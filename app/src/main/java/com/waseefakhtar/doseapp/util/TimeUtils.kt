package com.waseefakhtar.doseapp.util

import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.extension.toFormattedString
import java.util.concurrent.TimeUnit
import kotlin.math.abs


fun getTimeRemaining(medication: Medication): Long {
    return if (medication.date.toFormattedString() == medication.endDate.toFormattedString()) {
        0L
    } else {
        val dateBeforeInMs: Long = medication.date.time
        val dateAfterInMs: Long = medication.endDate.time
        val timeDiff = abs(dateAfterInMs - dateBeforeInMs)
        (TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS) + 1L)
    }
}