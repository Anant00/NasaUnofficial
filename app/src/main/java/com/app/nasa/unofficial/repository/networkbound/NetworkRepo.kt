package com.app.nasa.unofficial.repository.networkbound

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.app.nasa.unofficial.api.apimodel.NasaImages
import com.app.nasa.unofficial.api.apiservice.Api
import com.app.nasa.unofficial.utils.DateRangeUtils
import com.app.nasa.unofficial.utils.Resource
import com.app.nasa.unofficial.utils.toLiveData
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.pow

class NetworkRepo
@Inject constructor(
    private val api: Api,
    private var date: String,
    private var endDate: String
) {
    fun fetchImages(): LiveData<Resource<List<NasaImages>>> {
        val nasaImages: MediatorLiveData<Resource<List<NasaImages>>> = MediatorLiveData()
        nasaImages.postValue(Resource.loading("Loading...", null))
        val source = api.getImages(
            startDate = date,
            endDate = endDate
        )
            .subscribeOn(Schedulers.io())
            .retryWhen { errors ->
                errors.zipWith(
                    Flowable.range(1, 4),
                    BiFunction<Throwable, Int, Int> { error: Throwable, retryCount: Int ->
                        if (retryCount > 3) {
                            throw error
                        } else {
                            nasaImages.postValue(
                                Resource.loading(
                                    "Failed to Fetch. Retrying again...$retryCount of 3",
                                    null
                                )
                            )
                            retryCount
                        }
                    }
                ).flatMap { retryCount: Int ->
                    Flowable.timer(
                        2.0.pow(retryCount.toDouble()).toLong(),
                        TimeUnit.SECONDS
                    )
                }
            }
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

    fun loadNewData(): LiveData<Resource<List<NasaImages>>> {
        val newDates = DateRangeUtils.updateDate(date)
        val newStartDate = newDates.startDate
        val newEndDate = newDates.endDate
        this.date = newStartDate
        this.endDate = newEndDate
        return fetchImages()
    }
}
