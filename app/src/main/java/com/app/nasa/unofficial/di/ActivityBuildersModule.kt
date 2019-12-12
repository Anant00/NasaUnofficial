package com.app.nasa.unofficial.di

import com.app.nasa.unofficial.ui.activity.MainActivity
import com.app.nasa.unofficial.ui.fragments.PictureOfTheDayFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributePictureOfTheDayFragment(): PictureOfTheDayFragment
}
