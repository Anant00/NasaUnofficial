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
}
