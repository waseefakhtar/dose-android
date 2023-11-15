package com.waseefakhtar.doseapp.feature.home.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CalendarModel(
    val selectedDate: DateModel, // the date selected by the User. by default is Today.
    val visibleDates: List<DateModel> // the dates shown on the screen
) {

    val startDate: DateModel = visibleDates.first() // the first of the visible dates
    val endDate: DateModel = visibleDates.last() // the last of the visible dates

    data class DateModel(
        val date: Date,
        val isSelected: Boolean,
        val isToday: Boolean
    ) {
        val day: String = SimpleDateFormat("E", Locale.getDefault()).format(date) ?: ""
    }
}
