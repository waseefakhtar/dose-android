package com.waseefakhtar.doseapp.feature.home.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class CalendarModel(
    val selectedDate: DateModel, // the date selected by the User. by default is Today.
    val visibleDates: List<DateModel> // the dates shown on the screen
) {

    val startDate: DateModel = visibleDates.first() // the first of the visible dates
    val endDate: DateModel = visibleDates.last() // the last of the visible dates

    data class DateModel(
        val date: LocalDate,
        val isSelected: Boolean,
        val isToday: Boolean
    ) {
        val day: String
            get() = DateTimeFormatter
                .ofPattern("E", Locale.getDefault())
                .format(date) ?: ""
    }
}
