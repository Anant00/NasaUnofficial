package com.app.nasa.unofficial.di

import android.app.Application
import androidx.room.Room
import com.app.nasa.unofficial.api.apiservice.Api
import com.app.nasa.unofficial.db.ImageDatabase
import com.app.nasa.unofficial.db.ImagesDao
import com.app.nasa.unofficial.repository.local.LocalDatabaseRepository
import com.app.nasa.unofficial.repository.networkbound.NetworkRepo
import com.app.nasa.unofficial.utils.Constants
import com.app.nasa.unofficial.utils.DateRangeUtils
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [ViewModelsModule::class])
class AppModule {

    @Module
    companion object {
        @Singleton
        @Provides
        @JvmStatic
        fun provideApiService(client: OkHttpClient): Api {
            val newClient = with(client.newBuilder()) {
                connectTimeout(1, TimeUnit.MINUTES)
                readTimeout(1, TimeUnit.MINUTES)
                writeTimeout(1, TimeUnit.MINUTES)
            }.build()
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(newClient)
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
        fun provideNetworkRepo(
            api: Api, @Named("startDate") startDate: String,
            @Named("endDate") endDate: String, imagesDao: ImagesDao
        ): NetworkRepo {
            return NetworkRepo(
                api,
                startDate,
                endDate,
                imagesDao
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
            return DateRangeUtils.getOneMonthOldDate()
        }

        @JvmStatic
        @Provides
        @Singleton
        fun provideDb(app: Application): ImageDatabase {
            return Room
                .databaseBuilder(app, ImageDatabase::class.java, "nasa.db")
                .fallbackToDestructiveMigration()
                .build()
        }

        @Singleton
        @JvmStatic
        @Provides
        fun provideImageDao(db: ImageDatabase): ImagesDao {
            return db.imageDao()
        }

        @Singleton
        @JvmStatic
        @Provides
        fun provideLocalRepo(imagesDao: ImagesDao): LocalDatabaseRepository {
            return LocalDatabaseRepository(imagesDao)
        }
    }
}
