package com.app.nasa.unofficial.utils

import java.text.SimpleDateFormat
import java.util.*

object DateRangeUtils {

    val dateFormat by lazy {
        SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    }

    fun getTodayDate(): String {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT-8"))
        val cal = Calendar.getInstance()
        return dateFormat.format(cal.time)
    }

    fun getOneMonthOldDate(): String {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT-8"))
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -1)
        return dateFormat.format(cal.time)
    }

    fun getNewDates(startDate: String, days: Int): String {
        val date = dateFormat.parse(startDate)
        val cal = Calendar.getInstance()
        date?.let {
            cal.time = it
        }
        cal.add(Calendar.DAY_OF_MONTH, days)
        return dateFormat.format(cal.time)
    }

    fun updateDate(oldStartDate: String): DateObj {
        val newStartDate = getNewDates(oldStartDate, -16)
        val newEndDate = getNewDates(oldStartDate, -1)
        return DateObj(newStartDate, newEndDate)
    }

    fun updateDates(latestEndDate: String): DateObj {
        val newDates = updateDate(latestEndDate)
        val newStartDate = newDates.newStartDate
        val newEndDate = newDates.newEndDate
        return DateObj(newStartDate, newEndDate)
    }

    data class DateObj(var newStartDate: String, var newEndDate: String)
}
