package com.app.nasa.unofficial

import com.app.nasa.unofficial.utils.DateRangeUtils
import org.junit.Assert
import org.junit.Test

class NetworkRepoTest {

    @Test
    fun date_isToday() {
        Assert.assertEquals(DateRangeUtils.getDaysBackDate(), "2019-11-08")
    }
}
