package com.app.nasa.unofficial.repository.networkbound


import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.app.nasa.unofficial.utils.Resource

abstract class NetworkBoundResources<ResultType, RequestType> {

    init {

    }

    protected open fun onFetchFailed() {}


    @WorkerThread
    protected open fun processResponse(response: Resource<RequestType>) = response.data

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @WorkerThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @WorkerThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @WorkerThread
    protected abstract fun createCall(): LiveData<Resource<RequestType>>
}