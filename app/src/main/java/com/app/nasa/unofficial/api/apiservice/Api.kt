package com.app.nasa.unofficial.api.apiservice

import com.app.nasa.unofficial.api.apimodel.NasaImages
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("apod?api_key=BQWp2dHaRytHEV7UtZv1t9vOvm9N03u7aXQ8Cgn7")
    fun getImages(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): Flowable<List<NasaImages>>
}
