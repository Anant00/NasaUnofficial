package com.app.nasa.unofficial.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateRangeUtils {

    fun getTodayDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        dateFormat.timeZone = TimeZone.getTimeZone("America/California")
        val date = Date()
        return dateFormat.format(date)
    }

    fun getDaysBackDate(): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, -15)
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        formatter.timeZone = TimeZone.getTimeZone("America/California")
        return formatter.format(cal.time)
    }

    private fun getNewDates(startDate: String, daysAgo: Int): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val date = dateFormat.parse(startDate)
        val cal: Calendar = Calendar.getInstance()
        cal.time = date!!
        cal.add(Calendar.DAY_OF_MONTH, -daysAgo)
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        return formatter.format(cal.time)
    }

    fun updateDate(oldStartDate: String): DateObj {
        val newEndDate = getNewDates(oldStartDate, 1)
        val newStartDate = getNewDates(oldStartDate, 16)
        return DateObj(newStartDate, newEndDate)
    }

    data class DateObj(var startDate: String, var endDate: String)
}
