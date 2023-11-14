package com.waseefakhtar.doseapp.feature.home.data

import android.os.Build
import com.waseefakhtar.doseapp.feature.home.model.CalendarModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors
import java.util.stream.Stream

class CalendarDataSource {

    val today: LocalDate
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate.now()
            } else {
                TODO("VERSION.SDK_INT < O")
            }
        }


    fun getData(startDate: LocalDate = today, lastSelectedDate: LocalDate): CalendarModel {
        val firstDayOfWeek = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startDate.with(DayOfWeek.MONDAY)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val endDayOfWeek = firstDayOfWeek.plusDays(7)
        val visibleDates = getDatesBetween(firstDayOfWeek, endDayOfWeek)
        return toCalendarModel(visibleDates, lastSelectedDate)
    }

    private fun getDatesBetween(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
        val numOfDays = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ChronoUnit.DAYS.between(startDate, endDate)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        return Stream.iterate(startDate) { date ->
            date.plusDays(/* daysToAdd = */ 1)
        }
            .limit(numOfDays)
            .collect(Collectors.toList())
    }

    private fun toCalendarModel(
        dateList: List<LocalDate>,
        lastSelectedDate: LocalDate
    ): CalendarModel {
        return CalendarModel(
            selectedDate = toItemUiModel(lastSelectedDate, true),
            visibleDates = dateList.map {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    toItemUiModel(it, it.isEqual(lastSelectedDate))
                } else {
                    TODO("VERSION.SDK_INT < O")
                }
            },
        )
    }

    private fun toItemUiModel(date: LocalDate, isSelectedDate: Boolean) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        CalendarModel.Date(
            isSelected = isSelectedDate,
            isToday = date.isEqual(today),
            date = date,
        )
    } else {
        TODO("VERSION.SDK_INT < O")
    }
}