package com.waseefakhtar.doseapp.extension

import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Locale

fun Instant.toFormattedString(): String {
    val sdf = SimpleDateFormat("LLLL dd, yyyy", Locale.getDefault())
    return sdf.format(this)
}
