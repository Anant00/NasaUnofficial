package com.app.nasa.unofficial.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.app.nasa.unofficial.repository.networkbound.NetworkRepo
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers

class MainViewModel
@Inject constructor(private val repo: NetworkRepo) : ViewModel() {

    private val _page = MutableLiveData<Int>()
    private val initialPage = MutableLiveData<Int>()

    init {
        initialPage.value = 0
    }

    val loadInitialData = initialPage.switchMap {
        liveData(Dispatchers.IO) {
            val data = repo.fetchImages()
            emitSource(data)
        }
    }

    val loadMoreData = _page.switchMap {
        liveData(Dispatchers.IO) {
            val data = repo.loadNewData()
            emitSource(data)
        }
    }

    fun incrementPage(page: Int) {
        if (page != _page.value) {
            _page.postValue(page)
        }
    }
}
