package com.app.nasa.unofficial.di

import com.app.nasa.unofficial.ui.fragments.PictureOfTheDayFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributePODFragment(): PictureOfTheDayFragment
}