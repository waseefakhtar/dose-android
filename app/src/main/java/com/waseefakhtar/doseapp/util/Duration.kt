package com.waseefakhtar.doseapp.util

import java.util.Calendar
import java.util.Date
import kotlin.math.abs

enum class Duration(val days: Int, val displayName: String) {
    ONE_DAY(1, "1 Day"),
    THREE_DAYS(3, "3 Days"),
    ONE_WEEK(7, "1 Week"),
    TWO_WEEKS(14, "2 Weeks"),
    ONE_MONTH(30, "1 Month"),
    TWO_MONTHS(60, "2 Months"),
    THREE_MONTHS(90, "3 Months"),
    SIX_MONTHS(180, "6 Months"),
    CUSTOM(-1, "Custom"); // For user-selected dates

    fun calculateEndDate(startDate: Date): Date {
        if (this == CUSTOM) return startDate
        
        val calendar = Calendar.getInstance()
        calendar.time = startDate
        calendar.add(Calendar.DAY_OF_YEAR, days)
        return calendar.time
    }

    companion object {
        fun getDefaultDuration() = ONE_MONTH

        fun calculateDurationFromDates(startDate: Date, endDate: Date): Duration {
            val diffInMillis = endDate.time - startDate.time
            val diffInDays = abs(diffInMillis / (24 * 60 * 60 * 1000))
            
            return values()
                .filter { it != CUSTOM }
                .firstOrNull { it.days == diffInDays.toInt() }
                ?: CUSTOM
        }
    }
} 