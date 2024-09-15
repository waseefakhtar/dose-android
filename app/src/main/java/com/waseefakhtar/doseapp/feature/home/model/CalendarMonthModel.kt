package com.waseefakhtar.doseapp.feature.home.model

import java.util.Date

data class CalendarMonthModel(
    val selectedDate: MonthModel, // the date selected by the User. by default is Today.
    val visibleDates: List<MonthModel> // the dates shown on the screen
) {
    val startDate: MonthModel = visibleDates.first() // the first of the visible dates
    val endDate: MonthModel = visibleDates.last() // the last of the visible dates

    data class MonthModel(
        val date: Date,
        // 1. Green : All medicine taken
        // 2. Yellow : Some are taken some are not
        // 3. Red : All missed
        // 4. Surface : No data
        val status: Int
    )
}
