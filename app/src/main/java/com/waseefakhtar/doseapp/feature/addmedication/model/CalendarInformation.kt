package com.waseefakhtar.doseapp.feature.addmedication.model

import android.os.Parcelable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.MissingFormatArgumentException

class CalendarInformation(private val calendar: Calendar) {

    val dateInformation = TimeInformation(
        hour = calendar.get(Calendar.HOUR_OF_DAY),
        minute = calendar.get(Calendar.MINUTE)
    )

    fun getTimeInMillis() = calendar.timeInMillis

    fun getDateFormatted(pattern: String): String {
        return try {
            SimpleDateFormat(pattern, Locale.getDefault()).format(calendar.time)
        } catch (ex: MissingFormatArgumentException) {
            throw ex
        }
    }

    inner class TimeInformation(
        val hour: Int,
        val minute: Int,
    ) {
        operator fun component1(): Int = hour
        operator fun component2(): Int = minute
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

        fun getStateListSaver() = Saver<MutableList<CalendarInformation>, MutableList<Calendar>>(
            save = { state ->
                state.map { it.calendar }.toMutableList()
            },
            restore = {
                it.map { CalendarInformation(it) }.toMutableList()
            }
        )
    }
}
