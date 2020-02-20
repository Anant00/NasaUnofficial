package com.app.nasa.unofficial

import com.app.nasa.unofficial.db.ImageDatabase
import dagger.android.support.DaggerAppCompatActivity
import org.junit.After
import org.junit.Assert
import org.junit.Test

class ImagesDaoTest : DaggerAppCompatActivity() {
    private lateinit var imagesDao: ImageDatabase

//    @Before
//    fun initDb() {
//        imagesDao = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
//            imagesDao::class.java).build()
//    }

    @After
    fun closeDb() {
        imagesDao.close()
    }

    @Test
    fun `latest database date saved`() {
        Assert.assertEquals("27-01-2020", imagesDao.imageDao().getAllImagesFromDatabase())
    }
}