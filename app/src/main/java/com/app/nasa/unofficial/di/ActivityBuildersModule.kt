package com.app.nasa.unofficial.di

import com.app.nasa.unofficial.di.main.MainActivityModule
import com.app.nasa.unofficial.di.main.MainScope
import com.app.nasa.unofficial.ui.activity.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @MainScope
    @ContributesAndroidInjector(
        modules = [FragmentBuildersModule::class, MainActivityModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity
}
