package com.app.nasa.unofficial.repository.local

import com.app.nasa.unofficial.api.apimodel.NasaImages
import com.app.nasa.unofficial.db.ImagesDao
import io.reactivex.Flowable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocalDatabaseRepository
@Inject
constructor(private val imagesDao: ImagesDao) {

    fun getAllData(): Flowable<List<NasaImages>> {
        return imagesDao.getAllData()
    }

    fun insertAllData(imageList: List<NasaImages>) {
        CoroutineScope(IO).launch {
            imagesDao.insertAll(imageList)
        }
    }

    fun deleteAllData() {
        CoroutineScope(IO).launch {
            imagesDao.deleteAll()
        }
    }
}

