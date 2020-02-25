package com.app.nasa.unofficial.viewmodel

import androidx.lifecycle.*
import com.app.nasa.unofficial.repository.networkbound.NetworkRepo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel
@Inject
constructor(private val repo: NetworkRepo) : ViewModel() {

    private val _page = MutableLiveData<Int>()
    val page: LiveData<Int>
        get() = _page

    private val _initialPage = MutableLiveData<Int>()
    val initialPage: LiveData<Int>
        get() = _initialPage

    init {
        viewModelScope.launch(IO) {
            _initialPage.postValue(0)
        }
    }

    val loadInitialData = liveData(IO) {
        emitSource(repo.loadData())
    }

    val loadMoreData = _page.switchMap {
        liveData(IO) {
            emitSource(repo.loadMoreData())
        }
    }

    fun incrementPage(page: Int) {
        if (page != _page.value) {
            _page.postValue(page)
        }
    }

    /* use only for testing... Use testCoroutineDispatchers in future and pass the dispatcher in
        viewModel
    */
    val loadMoreDataOnMainThread = _page.switchMap {
        repo.loadMoreData()
    }

}