package com.waseefakhtar.doseapp.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.waseefakhtar.doseapp.util.TimesOfDay
import java.util.*


class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
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