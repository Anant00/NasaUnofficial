package com.app.nasa.unofficial.utilsTest

import com.app.nasa.unofficial.utils.DateRangeUtils
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class DateRangeUtilTest {
    private lateinit var todayDate: String
    private lateinit var dateFormat: SimpleDateFormat

    @Before
    fun `get current date of US from dateRangeUtil`() {
        todayDate = DateRangeUtils.getTodayDate()
    }

    @Test
    fun `is date today`() {
        Assert.assertEquals(todayDate, "2020-01-30")
    }

    @Test
    fun `is date one month back`() {
        Assert.assertEquals(DateRangeUtils.getOneMonthOldDate(), "2019-12-30")
    }

    @Before
    fun `set date format`() {
        dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    }

    @Test
    fun `is date format yyy-MM-dd`() {
        Assert.assertEquals(DateRangeUtils.dateFormat, dateFormat)
    }

    @Test
    fun `subtract days from current date to get new date`() {
        Assert.assertEquals(DateRangeUtils.getNewDates("2020-12-30", -5), "2020-12-25")
    }

    @Test
    fun `add date to current date`() {
        Assert.assertEquals(DateRangeUtils.getNewDates("2020-01-30", +5), "2020-02-04")
    }

    @Test
    fun `add nothing to current date`() {
        Assert.assertEquals(DateRangeUtils.getNewDates("2020-01-30", 0), "2020-01-30")
    }

    @Test
    fun `update new dates test`() {
        val startDate = "2020-01-18"
        val (newStartDate, newEndDate) = DateRangeUtils.updateDate(startDate)
        Assert.assertEquals(newEndDate, "2020-01-17")
        Assert.assertEquals(newStartDate, "2020-01-02")
    }

}