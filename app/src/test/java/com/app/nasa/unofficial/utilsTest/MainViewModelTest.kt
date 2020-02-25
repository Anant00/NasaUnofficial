package com.app.nasa.unofficial.utilsTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.app.nasa.unofficial.repository.networkbound.NetworkRepo
import com.app.nasa.unofficial.utilsTest.extensions.mock
import com.app.nasa.unofficial.viewmodel.MainViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var mainViewModel: MainViewModel
    private val networkRepo = mock(NetworkRepo::class.java)

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(networkRepo)
    }

    @Test
    fun `is viewModel emitting the value as soon as we create the object of it`() {
        val observer = mock<Observer<Int>>()
        mainViewModel.initialPage.observeForever(observer)
        verify(observer).onChanged(0)
    }

    @Test
    fun `check if next page is updated on called`() {
        val observer = mock<Observer<Int>>()
        verifyZeroInteractions(observer)
        mainViewModel.page.observeForever(observer)
        mainViewModel.incrementPage(1)
        verify(observer).onChanged(1)
        /*
            should not update value ( postValue() ) if current value in viewModel is equal to
            previous page as it will call the loadMoreData again.
        */
        mainViewModel.incrementPage(1)
        verifyNoMoreInteractions(observer)
        /*
          update the value again (if not equal to previous value)
         */
        mainViewModel.incrementPage(2)
        verify(observer).onChanged(2)
    }

    @Test
    fun `check if load more data is called on each UNIQUE page increment`() {
        mainViewModel.loadMoreDataOnMainThread.observeForever(mock())
        verifyNoMoreInteractions(networkRepo)
        mainViewModel.incrementPage(1)
        verify(networkRepo).loadMoreData()
        reset(networkRepo)
        mainViewModel.incrementPage(2)
        verify(networkRepo).loadMoreData()
        /*
          same @Param page = 2, should not call the method again.
        */
        mainViewModel.incrementPage(2)
        verifyNoMoreInteractions(networkRepo)
    }
}
