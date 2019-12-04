package com.app.nasa.unofficial.di

import com.app.nasa.unofficial.api.apiservice.Api
import com.app.nasa.unofficial.repository.networkbound.NetworkRepo
import com.app.nasa.unofficial.utils.Constants
import com.app.nasa.unofficial.utils.DateRangeUtils
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module(includes = [ViewModelsModule::class])
class AppModule {

    @Module
    companion object {
        @Singleton
        @Provides
        @JvmStatic
        fun provideApiService(client: OkHttpClient): Api {
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(Api::class.java)
        }

        @Singleton
        @Provides
        @JvmStatic
        fun provideOkHttpClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return OkHttpClient.Builder().addInterceptor(interceptor).build()
        }

        @Provides
        @JvmStatic
        fun provideNetworkRepo(api: Api, @Named("startDate") startDate: String, @Named("endDate") endDate: String): NetworkRepo {
            return NetworkRepo(
                api,
                startDate,
                endDate
            )
        }

        @Singleton
        @Provides
        @JvmStatic
        @Named("endDate")
        fun provideTodayDate(): String {
            return DateRangeUtils.getTodayDate()
        }

        @Singleton
        @Provides
        @JvmStatic
        @Named("startDate")
        fun provideEndDate(): String {
            return DateRangeUtils.getDaysBackDate()
        }
    }
}
