package com.app.nasa.unofficial.repository.networkbound

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.app.nasa.unofficial.api.apimodel.NasaImages
import com.app.nasa.unofficial.api.apiservice.Api
import com.app.nasa.unofficial.repository.Resource
import com.app.nasa.unofficial.utils.toLiveData
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NetworkRepo
@Inject constructor(
    private val api: Api,
    private val date: String,
    private val endDate: String
) {
    fun fetchImages(): LiveData<Resource<List<NasaImages>>> {
        val nasaImages: MediatorLiveData<Resource<List<NasaImages>>> = MediatorLiveData()
        nasaImages.postValue(Resource.loading(null))
        val source = api.getImages(
            startDate = date,
            endDate = endDate
        )
            .subscribeOn(Schedulers.io())
            .flatMapIterable { it.asReversed() }
            .filter {
                it.mediaType != "video" && it.mediaType != "gif"
            }
            .toList()
            .toFlowable()
            .map {
                Resource.success(it)
            }
            .doOnError {
                Resource.error(it.localizedMessage, null)
            }
            .onErrorReturn {
                Resource.error(it.localizedMessage, null)
            }
            .toLiveData()

        nasaImages.addSource(source) {
            nasaImages.value = it
            nasaImages.removeSource(source)
        }
        return nasaImages
    }
}
