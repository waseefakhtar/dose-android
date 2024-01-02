package com.waseefakhtar.doseapp.feature.addmedication

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import com.waseefakhtar.doseapp.extension.millisSinceEpoch
import com.waseefakhtar.doseapp.util.Recurrence
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

object StateSavers {

    val localDateSaver = object : Saver<LocalDate, Long> {

        override fun restore(value: Long): LocalDate = Instant.ofEpochMilli(value)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        override fun SaverScope.save(value: LocalDate): Long = value.millisSinceEpoch
    }

    val recurrenceSaver = object : Saver<Recurrence, String> {

        override fun restore(value: String): Recurrence = Recurrence.valueOf(value)

        override fun SaverScope.save(value: Recurrence): String = value.name
    }

    val localTimeSaver = object : Saver<LocalTime, Long> {

        override fun restore(value: Long): LocalTime? = LocalTime.ofSecondOfDay(value)

        override fun SaverScope.save(value: LocalTime): Long = value.toSecondOfDay().toLong()

    }

    val localListTimeSaver = object : Saver<MutableList<LocalTime>, List<Long>> {

        override fun restore(value: List<Long>): MutableList<LocalTime> =
            value.map(LocalTime::ofSecondOfDay).toMutableList()

        override fun SaverScope.save(value: MutableList<LocalTime>): List<Long> =
            value.map(LocalTime::toSecondOfDay).map(Int::toLong)

    }
}