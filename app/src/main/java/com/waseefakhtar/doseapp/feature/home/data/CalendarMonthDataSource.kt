package com.waseefakhtar.doseapp.feature.home.data

import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.extension.hasPassed
import com.waseefakhtar.doseapp.extension.toDate
import com.waseefakhtar.doseapp.feature.home.model.CalendarMonthModel
import java.util.Calendar
import java.util.Date

class CalendarMonthDataSource {

    val today: Date
        get() {
            return Date()
        }

    private fun toCalendarMonthModel(
        dateList: List<Date>, lastSelectedDate: Date, medicationList: List<Medication>
    ): CalendarMonthModel {
        var list = mutableListOf<Int>()
        if (medicationList.isNotEmpty()) {
            list = getMonthlyDateStats(dateList[0], dateList.last(), medicationList)
        }
        return CalendarMonthModel(selectedDate = toMonthItemModel(
            lastSelectedDate,
            -1
        ), // Not Applicable
            visibleDates = dateList.mapIndexed { index, date ->
                toMonthItemModel(
                    date = date, status = if (list.isNotEmpty()) {
                        list[index]
                    } else 4
                )
            })
    }

    fun getMonthData(
        startDate: Date = today, lastSelectedDate: Date, dataList: List<Medication>
    ): CalendarMonthModel {
        val medicationList = dataList.filter { it.medicationTime.hasPassed() }
        val calendar = Calendar.getInstance()
        calendar.time = startDate

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfMonth = calendar.time

        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val lastDayOfMonth = calendar.time

        val visibleDates = getDatesBetween(firstDayOfMonth, lastDayOfMonth)
        return toCalendarMonthModel(visibleDates, lastSelectedDate, medicationList)
    }

    private fun toMonthItemModel(date: Date, status: Int): CalendarMonthModel.MonthModel {
        return CalendarMonthModel.MonthModel(
            date = date, status = status
        )
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

    fun isCurrentMonth(date: Date): Boolean {
        val currentCalendar = Calendar.getInstance()
        val dateCalendar = Calendar.getInstance()
        dateCalendar.time = date

        return dateCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                dateCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)
    }

    private fun getMonthlyDateStats(
        startDate: Date, endDate: Date, medicationList: List<Medication>
    ): MutableList<Int> {
        val calendar = Calendar.getInstance()
        calendar.time = startDate

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val result = MutableList(daysInMonth) { Pair(0, 0) }

        for (medication in medicationList) {
            if (medication.medicationTime in startDate..endDate) {
                calendar.time = medication.medicationTime
                val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH) - 1

                val (yesCount, noCount) = result[dayOfMonth]
                val data = if (medication.medicationTaken) {
                    Pair(yesCount + 1, noCount)
                } else {
                    Pair(yesCount, noCount + 1)
                }
                result[dayOfMonth] = data
            }
        }

        val calculateMedication = MutableList(result.size) { 4 }

        for (i in 0..<calculateMedication.size) {
            calculateMedication[i] = when (true) {
                (result[i].first == 0 && result[i].second == 0) -> 4
                (result[i].first != 0 && result[i].second != 0) -> 2
                (result[i].first == 0) -> 3
                (result[i].second == 0) -> 1
                else -> 4
            }
        }
        return calculateMedication
    }

    fun getLastSelectedDate(dateString: String): Date {
        return dateString.toDate() ?: today
    }
}
