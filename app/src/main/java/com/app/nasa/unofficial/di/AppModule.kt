package com.app.nasa.unofficial.di

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Module
    companion object {
        @Singleton
        @Provides
        @JvmStatic
        fun string(): String {
            return "Hello, I am injected"
        }
    }
}
