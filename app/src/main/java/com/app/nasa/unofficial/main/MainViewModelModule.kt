package com.app.nasa.unofficial.main

import androidx.lifecycle.ViewModel
import com.app.nasa.unofficial.di.ViewModelKey
import com.app.nasa.unofficial.viewmodel.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel
}
