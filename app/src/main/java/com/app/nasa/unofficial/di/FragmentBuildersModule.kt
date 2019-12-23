package com.app.nasa.unofficial.di

import com.app.nasa.unofficial.di.main.MainScope
import com.app.nasa.unofficial.ui.fragments.MarsFragment
import com.app.nasa.unofficial.ui.fragments.PictureOfTheDayFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributePODFragment(): PictureOfTheDayFragment

    @ContributesAndroidInjector
    abstract fun contributeMarsFragment(): MarsFragment
}
