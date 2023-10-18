package com.waseefakhtar.doseapp.feature.addmedication.model

import androidx.compose.runtime.saveable.Saver
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.MissingFormatArgumentException

class CalendarInformation(private val calendar: Calendar) {

    val dateInformation = DateInformation(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH),
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    )

    fun getTimeInMillis() = calendar.timeInMillis

    fun getDateFormatted(pattern: String): String {
        return try {
            SimpleDateFormat(pattern, Locale.getDefault()).format(calendar.time)
        } catch (ex: MissingFormatArgumentException) {
            throw ex
        }
    }

    inner class DateInformation(
        val year: Int,
        val month: Int,
        val dayOfMonth: Int
    ) {
        operator fun component1(): Int = year
        operator fun component2(): Int = month
        operator fun component3(): Int = dayOfMonth
    }

    companion object {
        fun getStateSaver() = Saver<CalendarInformation, Calendar>(
            save = { state ->
                state.calendar
            },
            restore = {
                CalendarInformation(it)
            }
        )
    }
}
