package com.waseefakhtar.doseapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.waseefakhtar.doseapp.util.TimesOfDay
import java.time.Instant
import java.util.Collections
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromInstant(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun instantToTimeStamp(instant: Instant?): Long? {
        return instant?.toEpochMilli()
    }

    var gson = Gson()
    @TypeConverter
    fun stringToTimesOfDay(data: String?): List<TimesOfDay?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType = object : TypeToken<List<TimesOfDay?>?>() {}.type
        return gson.fromJson<List<TimesOfDay?>>(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<TimesOfDay?>?): String? {
        return gson.toJson(someObjects)
    }
}
