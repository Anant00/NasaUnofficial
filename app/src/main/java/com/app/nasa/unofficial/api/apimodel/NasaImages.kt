package com.app.nasa.unofficial.api.apimodel

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "dailyImage")
@Parcelize
data class NasaImages(
    @field:Json(name = "copyright")
    var copyright: String? = null,
    @field:Json(name = "date")
    @PrimaryKey
    var date: String,
    @field:Json(name = "explanation")
    var explanation: String? = null,
    @field:Json(name = "hdurl")
    var hdurl: String? = null,
    @field:Json(name = "media_type")
    var mediaType: String? = null,
    @field:Json(name = "title")
    var title: String? = null,
    @field:Json(name = "url")
    var url: String? = null
) : Parcelable
