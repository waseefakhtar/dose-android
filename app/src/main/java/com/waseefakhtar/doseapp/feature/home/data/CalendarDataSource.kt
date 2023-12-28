package com.waseefakhtar.doseapp.feature.home.data

import com.waseefakhtar.doseapp.extension.toDate
import com.waseefakhtar.doseapp.extension.toFormattedDateString
import com.waseefakhtar.doseapp.feature.home.model.CalendarModel
import java.util.Calendar
import java.util.Date

class CalendarDataSource {

    val today: Date
        get() {
            return Date()
        }

    fun getLastSelectedDate(dateString: String): Date {
        return dateString.toDate() ?: today
    }
    fun getData(startDate: Date = today, lastSelectedDate: Date): CalendarModel {
        val calendar = Calendar.getInstance()
        calendar.time = startDate

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val firstDayOfWeek = calendar.time

        calendar.add(Calendar.DAY_OF_YEAR, 6)
        val endDayOfWeek = calendar.time

        val visibleDates = getDatesBetween(firstDayOfWeek, endDayOfWeek)
        return toCalendarModel(visibleDates, lastSelectedDate)
    }

    private fun getDatesBetween(startDate: Date, endDate: Date): List<Date> {
        val dateList = mutableListOf<Date>()
        val calendar = Calendar.getInstance()
        calendar.time = startDate

        while (calendar.time <= endDate) {
            dateList.add(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return dateList
    }

    private fun toCalendarModel(
        dateList: List<Date>,
        lastSelectedDate: Date
    ): CalendarModel {
        return CalendarModel(
            selectedDate = toItemModel(lastSelectedDate, true),
            visibleDates = dateList.map {
                toItemModel(
                    date = it,
                    isSelectedDate = it.toFormattedDateString() == lastSelectedDate.toFormattedDateString()
                )
            }
        )
    }

    private fun toItemModel(date: Date, isSelectedDate: Boolean): CalendarModel.DateModel {
        return CalendarModel.DateModel(
            isSelected = isSelectedDate,
            isToday = isToday(date),
            date = date
        )
    }

    private fun isToday(date: Date): Boolean {
        val todayDate = today
        return date.toFormattedDateString() == todayDate.toFormattedDateString()
    }
}
