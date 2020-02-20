package com.app.nasa.unofficial.repository.networkbound

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import com.app.nasa.unofficial.utils.Resource
import com.app.nasa.unofficial.utils.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class NetworkBoundResources<ResultType, RequestType> {
    private val result = MediatorLiveData<Resource<ResultType>>()
    init {
        val dataSource = loadData()

        result.addSource(dataSource) { data ->
            result.removeSource(dataSource)
            if (shouldFetch(data)) {
                fetch(dbSource = dataSource)
            } else {
                CoroutineScope(IO).launch {
                    val localData = loadData()
                    withContext(Main) {
                        result.addSource(localData) {
                            setValue(Resource.success(it))
                        }
                    }
                }
            }
        }
    }

    private fun fetch(dbSource: LiveData<ResultType>) {
        result.addSource(dbSource) {
            setValue(Resource.loading("Loading", it))
            result.removeSource(dbSource)
        }
        val apiResponse = fetchFromNetwork()
        result.addSource(apiResponse) {
            when (it.status) {
                Status.SUCCESS -> {
                    CoroutineScope(IO).launch {
                        saveDataInLocalDb(processResponse(it))
                        val localData = loadData()
                        withContext(Main) {
                            result.addSource(localData) { newData ->
                                setValue(Resource.success(newData))
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    CoroutineScope(IO).launch {
                        val localData = loadData()
                        withContext(Main) {
                            result.addSource(localData) { oldData ->
                                setValue(Resource.error(it.message, oldData))
                            }
                        }
                    }
                }
                Status.LOADING -> {
                    CoroutineScope(IO).launch {
                        val localData = loadData()
                        withContext(Main) {
                            result.addSource(localData) { oldData ->
                                setValue(Resource.loading(it.message, oldData))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loadData(): LiveData<ResultType> {
        return loadFromDb().asLiveData()
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.postValue(newValue)
        }
    }

    protected open fun onFetchFailed() {
    }

    fun toLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: Resource<RequestType>) = response.data

    @WorkerThread
    protected abstract fun saveDataInLocalDb(item: RequestType?)

    @WorkerThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @WorkerThread
    protected abstract fun loadFromDb(): Flow<ResultType>

    @WorkerThread
    protected abstract fun fetchFromNetwork(): LiveData<Resource<RequestType>>
}
