package com.app.nasa.unofficial

import com.app.nasa.unofficial.repository.networkbound.NetworkRepo
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import javax.inject.Inject

@RunWith(MockitoJUnitRunner::class)
class NetworkRepoTest {

    @Mock
    @Inject
    lateinit var networkRepo: NetworkRepo

    @Test
    fun testing() {
        Mockito.`when`(networkRepo.getData()).thenReturn("get data")
        Assert.assertEquals(networkRepo.getData(), "get data")
    }
}
