package com.waseefakhtar.doseapp.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.waseefakhtar.doseapp.R
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.extension.toFormattedDateString
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

const val HOUR_MINUTE_FORMAT = "h:mm a"

@Composable
fun getTimeRemaining(
    medication: Medication,
    time: LocalDateTime = LocalDateTime.now()
): String {

    val timeDiff by remember {
        derivedStateOf {
            val medicationTime = medication.medicationTime
            val between = ChronoUnit.MILLIS.between(time, medicationTime)
            between.milliseconds
        }
    }

    // If the medication is scheduled for a future date, display days remaining
    if (medication.medicationTime.toFormattedDateString() != medication.endDate.toFormattedDateString()) {
        val daysRemaining = timeDiff.toDouble(DurationUnit.DAYS) + 1L
        return stringResource(
            id = R.string.time_remaining,
            daysRemaining,
            stringResource(id = R.string.days)
        )
    }

    // If the medication is scheduled for today, calculate time remaining in hours and minutes
    val hoursRemaining = timeDiff.toDouble(DurationUnit.HOURS)
    val minutesRemaining = timeDiff.toDouble(DurationUnit.MINUTES)
    return when {
        hoursRemaining > 1 -> stringResource(
            id = R.string.time_remaining,
            hoursRemaining,
            stringResource(id = R.string.hours)
        )

        minutesRemaining > 1 -> stringResource(
            id = R.string.time_remaining,
            minutesRemaining,
            stringResource(id = R.string.days)
        )

        else -> stringResource(id = R.string.take_dose_now)
    }
}
