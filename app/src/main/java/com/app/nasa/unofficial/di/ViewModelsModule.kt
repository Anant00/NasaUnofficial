package com.app.nasa.unofficial.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.nasa.unofficial.viewmodel.AppViewModelFactory
import com.app.nasa.unofficial.viewmodel.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindUserViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}
