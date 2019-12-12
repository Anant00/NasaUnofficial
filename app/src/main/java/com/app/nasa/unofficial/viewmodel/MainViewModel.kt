package com.app.nasa.unofficial.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.app.nasa.unofficial.api.apimodel.NasaImages
import com.app.nasa.unofficial.repository.networkbound.NetworkRepo
import com.app.nasa.unofficial.utils.Resource
import javax.inject.Inject

class MainViewModel
@Inject constructor(private var repo: NetworkRepo) : ViewModel() {

    private var imagesData: LiveData<Resource<List<NasaImages>>>? = null
    private val _page = MutableLiveData<Int>()

    init {
        imagesData = repo.fetchImages()
    }

    fun getImageData(): LiveData<Resource<List<NasaImages>>>? {
        return imagesData
    }

    val loadMoreData = Transformations
        .switchMap(_page) {
            repo.loadNewData()
        }

    fun incrementPage(page: Int) {
        if (page != _page.value) {
            _page.postValue(page)
        }
    }
}
