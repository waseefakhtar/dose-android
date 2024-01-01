package com.waseefakhtar.doseapp.data

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class Converters {

    private val zoneId = ZoneId.systemDefault()

    @TypeConverter
    fun fromTimestamp(value: Long): LocalDateTime {
        return Instant.ofEpochMilli(value)
            .atZone(zoneId)
            .toLocalDateTime()
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime): Long {
        return date.atZone(zoneId)
            .toInstant()
            .toEpochMilli()
    }
}
