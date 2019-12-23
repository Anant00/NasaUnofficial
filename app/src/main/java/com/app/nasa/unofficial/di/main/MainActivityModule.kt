package com.app.nasa.unofficial.di.main

import com.app.nasa.unofficial.ui.adapters.ImagesAdapter
import com.app.nasa.unofficial.ui.fragments.PictureOfTheDayFragment
import com.app.nasa.unofficial.utils.OnRecyclerViewItemClick
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Module
    companion object {

        @MainScope
        @JvmStatic
        @Provides
        fun provideRecyclerViewItemClickListener(): OnRecyclerViewItemClick {
            return PictureOfTheDayFragment()
        }

        @MainScope
        @JvmStatic
        @Provides
        fun provideImageAdapter(listener: OnRecyclerViewItemClick): ImagesAdapter {
            return ImagesAdapter(listener)
        }
    }
}
