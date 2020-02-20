package com.app.nasa.unofficial.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.nasa.unofficial.api.apimodel.NasaImages

@Database(entities = [NasaImages::class], version = 1)
abstract class ImageDatabase : RoomDatabase() {
    abstract fun imageDao(): ImagesDao
}