package com.app.nasa.unofficial.utilsTest

import com.app.nasa.unofficial.utils.Status
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class StatusTest {
    private lateinit var status: Status

    @Before
    fun `set status`() {
        val list = listOf(2, 4, 6)
        for (items in list) {
            status = if (items % 2 == 0) Status.SUCCESS
            else Status.ERROR
        }
    }

    @Test
    fun `test status success`() {
        Assert.assertEquals(Status.SUCCESS, status)
    }
}