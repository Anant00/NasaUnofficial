package com.app.nasa.unofficial.repository.networkbound

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.app.nasa.unofficial.api.apimodel.NasaImages
import com.app.nasa.unofficial.api.apiservice.Api
import com.app.nasa.unofficial.db.ImagesDao
import com.app.nasa.unofficial.utils.DateRangeUtils.updateDates
import com.app.nasa.unofficial.utils.Resource
import com.app.nasa.unofficial.utils.Status
import com.app.nasa.unofficial.utils.toLiveData
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.pow

class NetworkRepo
@Inject constructor(
    private val api: Api,
    private var startDate: String,
    private var endDate: String,
    private val imagesDao: ImagesDao
) {

    fun loadData(): LiveData<Resource<List<NasaImages>>> {
        return object : NetworkBoundResources<List<NasaImages>, List<NasaImages>>() {
            override fun saveDataInLocalDb(item: List<NasaImages>?) {
                CoroutineScope(IO).launch {
                    item?.let { imagesDao.insertAll(it) }
                }
            }

            override fun shouldFetch(data: List<NasaImages>?): Boolean {
                return data.isNullOrEmpty() || data[0].date != endDate
            }

            override fun loadFromDb(): Flow<List<NasaImages>> {
                return imagesDao.getAllImagesFromDatabase().distinctUntilChanged()
            }

            override fun fetchFromNetwork(): LiveData<Resource<List<NasaImages>>> {
                return fetchImages()
            }

        }.toLiveData()
    }

    private fun fetchImages(): LiveData<Resource<List<NasaImages>>> {
        val nasaImages: MediatorLiveData<Resource<List<NasaImages>>> = MediatorLiveData()
        val source = api.getImages(
            startDate = startDate,
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

    fun loadMoreData(): LiveData<Resource<List<NasaImages>>> {
        val latestEndDateFromDatabase = imagesDao.getLatestDateFromDatabase()
        val dates = updateDates(latestEndDateFromDatabase)
        this.startDate = dates.newStartDate
        this.endDate = dates.newEndDate
        val resource = MediatorLiveData<Resource<List<NasaImages>>>()
        val data = fetchImages()
        resource.addSource(data) {
            CoroutineScope(IO).launch {
                if (it.status == Status.SUCCESS) {
                    it.data?.let { imagesDao.insertAll(it) }
                }
                resource.postValue(it)
            }
        }
        return resource
    }
}
