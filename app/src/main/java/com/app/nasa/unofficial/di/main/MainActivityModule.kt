package com.app.nasa.unofficial.di.main

import com.app.nasa.unofficial.ui.adapters.ImagesAdapter
import com.app.nasa.unofficial.ui.fragments.PictureOfTheDayFragment
import com.app.nasa.unofficial.utils.OnItemClick
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Module
    companion object {

        @MainScope
        @JvmStatic
        @Provides
        fun provideRecyclerViewItemClickListener(): OnItemClick {
            return PictureOfTheDayFragment()
        }

        @MainScope
        @JvmStatic
        @Provides
        fun provideImageAdapter(listener: OnItemClick): ImagesAdapter {
            return ImagesAdapter(listener)
        }
    }
}
