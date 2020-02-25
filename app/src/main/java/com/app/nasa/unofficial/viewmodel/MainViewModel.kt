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
    private val initialPage = MutableLiveData<Int>()

    init {
        viewModelScope.launch(IO) {
            initialPage.postValue(0)
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
}
