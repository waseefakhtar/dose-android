package com.waseefakhtar.doseapp.feature.home.data

import com.waseefakhtar.doseapp.feature.home.model.CalendarModel
import java.time.LocalDate

class CalendarDataSource {

    val today: LocalDate
        get() = LocalDate.now()

    fun getData(startDate: LocalDate = today, lastSelectedDate: LocalDate): CalendarModel {
        val weekDayStart = startDate.dayOfWeek.value - 1L

        val firstDayOfWeek = startDate.minusDays(weekDayStart)

        val endDayOfWeek = startDate.plusDays(6L - weekDayStart)

        val visibleDates = getDatesBetween(firstDayOfWeek, endDayOfWeek)
        return toCalendarModel(visibleDates, lastSelectedDate)
    }

    private fun getDatesBetween(startDate: LocalDate, endDate: LocalDate): List<LocalDate> =
        buildList {
            var start = startDate
            while (start <= endDate) {
                add(start)
                start = start.plusDays(1)
            }
        }

    private fun toCalendarModel(
        dateList: List<LocalDate>,
        lastSelectedDate: LocalDate
    ): CalendarModel = CalendarModel(
        selectedDate = toItemModel(lastSelectedDate, true),
        visibleDates = dateList.map {
            toItemModel(it, it == lastSelectedDate)
        }
    )


    private fun toItemModel(date: LocalDate, isSelectedDate: Boolean): CalendarModel.DateModel {
        return CalendarModel.DateModel(
            isSelected = isSelectedDate,
            isToday = today == date,
            date = date
        )
    }

}
