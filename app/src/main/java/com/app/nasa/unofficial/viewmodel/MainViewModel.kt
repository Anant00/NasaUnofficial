package com.app.nasa.unofficial.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.nasa.unofficial.api.apimodel.NasaImages
import com.app.nasa.unofficial.repository.NetworkRepo
import com.app.nasa.unofficial.ui.Resource
import javax.inject.Inject

class MainViewModel
@Inject constructor(repo: NetworkRepo) : ViewModel() {

    private var imagesData: LiveData<Resource<List<NasaImages>>>? = null

    init {
        imagesData = repo.test()
    }

    fun getImageData(): LiveData<Resource<List<NasaImages>>>? {
        return imagesData
    }
}
