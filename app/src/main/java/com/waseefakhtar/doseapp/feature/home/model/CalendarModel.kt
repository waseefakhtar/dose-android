package com.waseefakhtar.doseapp.feature.home.model

import android.os.Build
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class CalendarModel(
    val selectedDate: Date, // the date selected by the User. by default is Today.
    val visibleDates: List<Date> // the dates shown on the screen
) {

    val startDate: Date = visibleDates.first() // the first of the visible dates
    val endDate: Date = visibleDates.last() // the last of the visible dates

    data class Date(
        val date: LocalDate,
        val isSelected: Boolean,
        val isToday: Boolean
    ) {
        val day: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date.format(DateTimeFormatter.ofPattern("E"))
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }
}