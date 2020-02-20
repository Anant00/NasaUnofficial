package com.app.nasa.unofficial

import com.app.nasa.unofficial.utils.DateRangeUtils
import org.junit.Assert
import org.junit.Test

class NetworkRepoTest {

    @Test
    fun date_isToday() {
        Assert.assertEquals(DateRangeUtils.getOneMonthOldDate(), "2019-11-08")
    }

    @Test
    fun `today date must be equal to US California's current date`() {
        Assert.assertEquals(DateRangeUtils.getTodayDate(), "2020-01-23")
    }
}
