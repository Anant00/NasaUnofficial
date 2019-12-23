package com.app.nasa.unofficial.di

import com.app.nasa.unofficial.main.MainScope
import com.app.nasa.unofficial.ui.fragments.MarsFragment
import com.app.nasa.unofficial.ui.fragments.PictureOfTheDayFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @MainScope
    @ContributesAndroidInjector
    abstract fun contributePODFragment(): PictureOfTheDayFragment

    @MainScope
    @ContributesAndroidInjector
    abstract fun contributeMarsFragment(): MarsFragment
}
