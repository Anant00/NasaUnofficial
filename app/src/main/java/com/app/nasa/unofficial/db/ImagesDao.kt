package com.app.nasa.unofficial.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.app.nasa.unofficial.api.apimodel.NasaImages
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow

@Dao
interface ImagesDao {
    @Insert(onConflict = REPLACE)
    fun insertAll(list: List<NasaImages>): List<Long>

    @Update
    fun update(nasaImages: NasaImages)

    @Query("DELETE FROM dailyImage")
    fun deleteAll()

    @Query("SELECT * FROM dailyImage ORDER BY date DESC")
    fun getAllImagesFromDatabase(): Flow<List<NasaImages>>

    @Query("SELECT * FROM dailyImage ORDER BY date DESC")
    fun getAllData(): Flowable<List<NasaImages>>

    @Query("SELECT date FROM dailyImage ORDER BY date ASC LIMIT 1")
    fun getStartDateFromDb(): String

    @Query("SELECT date FROM dailyImage ORDER BY date DESC LIMIT 1")
    fun getEndDateFromDb(): String
}