package com.waseefakhtar.doseapp.extension

import com.waseefakhtar.doseapp.R

data class Duration(
    val primary: Int,
    val primaryType: DurationType,
    val remainder: Int? = null,
    val remainderType: DurationType? = null,
)

fun Long.calculateDurationInDays(endMillis: Long): Int = ((endMillis - this) / (24 * 60 * 60 * 1000)).toInt() + 1

fun Long.formatDuration(endMillis: Long): Duration {
    val totalDays = calculateDurationInDays(endMillis)

    return when {
        totalDays >= 365 -> {
            val years = totalDays / 365
            val remainingDays = totalDays % 365
            when {
                remainingDays >= 30 ->
                    Duration(
                        years,
                        DurationType.YEARS,
                        remainingDays / 30,
                        DurationType.MONTHS,
                    )
                remainingDays > 0 ->
                    Duration(
                        years,
                        DurationType.YEARS,
                        remainingDays,
                        DurationType.DAYS,
                    )
                else -> Duration(years, DurationType.YEARS)
            }
        }
        totalDays >= 30 -> {
            val months = totalDays / 30
            val remainingDays = totalDays % 30
            if (remainingDays > 0) {
                Duration(months, DurationType.MONTHS, remainingDays, DurationType.DAYS)
            } else {
                Duration(months, DurationType.MONTHS)
            }
        }
        totalDays >= 7 -> {
            val weeks = totalDays / 7
            val remainingDays = totalDays % 7
            if (remainingDays > 0) {
                Duration(weeks, DurationType.WEEKS, remainingDays, DurationType.DAYS)
            } else {
                Duration(weeks, DurationType.WEEKS)
            }
        }
        else -> Duration(totalDays, DurationType.DAYS)
    }
}

enum class DurationType(
    val pluralResId: Int,
) {
    DAYS(R.plurals.duration_days),
    WEEKS(R.plurals.duration_weeks),
    MONTHS(R.plurals.duration_months),
    YEARS(R.plurals.duration_years),
}
