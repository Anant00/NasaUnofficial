package com.app.nasa.unofficial.utilsTest

import com.app.nasa.unofficial.utils.Resource
import com.app.nasa.unofficial.utils.Status
import org.junit.Assert
import org.junit.Test

class ResourcesTest {

    private var status = Status.LOADING
    private val message = "Ok"
    private val data = 1

    @Test
    fun `test resource status`() {
        Assert.assertEquals(Resource.loading("loading", null).status, status)
    }

    @Test
    fun `test resource message`() {
        Assert.assertEquals(Resource.loading("Ok", null).message, message)
    }

    @Test
    fun `test resource data`() {
        Assert.assertEquals(Resource.loading("Ok", 1).data, data)
    }
}