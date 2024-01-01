package com.waseefakhtar.doseapp.extension

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDateTime.toFormattedDateString(): String {
    val sdf = DateTimeFormatter.ofPattern("EEEE, MMMM dd", Locale.getDefault())
    return sdf.format(this)
}

fun LocalDate.toFormattedDateString(): String {
    val sdf = DateTimeFormatter.ofPattern("EEEE, MMMM dd", Locale.getDefault())
    return sdf.format(this)
}

/**
 * Formats a [LocalDateTime] object into readable date format of patten `MMMM dd`
 * Example: December 31
 */
fun LocalDateTime.toFormattedMonthDateString(): String {
    val sdf = DateTimeFormatter.ofPattern("MMMM dd", Locale.getDefault())
    return sdf.format(this)
}

fun LocalDate.toFormattedMonthDateString(): String {
    val sdf = DateTimeFormatter.ofPattern("MMMM dd", Locale.getDefault())
    return sdf.format(this)
}


/**
 * Formats a [LocalDateTime] object into readable date of format of pattern `dd`
 * Example: 31
 */
fun LocalDateTime.toFormattedDateShortString(): String {
    val sdf = DateTimeFormatter.ofPattern("dd", Locale.getDefault())
    return sdf.format(this)
}

fun LocalDate.toFormattedDateShortString(): String {
    val sdf = DateTimeFormatter.ofPattern("dd", Locale.getDefault())
    return sdf.format(this)
}


fun Long.toFormattedDateString(): String {
    val sdf = SimpleDateFormat("LLLL dd, yyyy", Locale.getDefault())
    return sdf.format(this)
}


/**
 * Formats a [LocalDateTime] object into readable time format of patten `HH:mm`
 * Example: 12:00
 */
fun LocalDateTime.toFormattedTimeString(): String {
    val timeFormat = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
    return timeFormat.format(this)
}

/**
 * Formats a [LocalTime] object into readable time format of patten `HH:mm`
 * Example: 12:00
 */
fun LocalTime.toFormattedTimeString(): String {
    val timeFormat = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
    return timeFormat.format(this)
}

val LocalDateTime.millisSinceEpoch: Long
    get() = atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

val LocalDate.millisSinceEpoch: Long
    get() = atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()


/**
 * Checks if the [LocalDateTime] has passed
 */
val LocalDateTime.hasPassed: Boolean
    get() = this < LocalDateTime.now()


//fun Date.hasPassed(): Boolean {
//    val calendar = Calendar.getInstance()
//    calendar.add(Calendar.SECOND, -1)
//    val oneSecondAgo = calendar.time
//    return time < oneSecondAgo.time
//}
