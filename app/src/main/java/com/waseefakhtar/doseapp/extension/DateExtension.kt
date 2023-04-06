package com.waseefakhtar.doseapp.extension

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.toFormattedString(): String {
    val date = Date.from(this)
    val sdf = SimpleDateFormat("LLLL dd, yyyy", Locale.getDefault())
    return sdf.format(date)
}
