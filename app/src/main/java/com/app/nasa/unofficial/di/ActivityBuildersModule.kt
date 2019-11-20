package com.app.nasa.unofficial.di

import com.app.nasa.unofficial.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(
        modules = [ViewModelModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity
}
