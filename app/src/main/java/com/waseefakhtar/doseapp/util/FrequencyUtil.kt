package com.waseefakhtar.doseapp.util

import com.waseefakhtar.doseapp.R

enum class Frequency(val stringResId: Int, val days: Int) {
    EVERYDAY(R.string.everyday, 1),
    EVERY_2_DAYS(R.string.every_n_days, 2),
    EVERY_3_DAYS(R.string.every_n_days, 3),
    EVERY_4_DAYS(R.string.every_n_days, 4),
    EVERY_5_DAYS(R.string.every_n_days, 5),
    EVERY_6_DAYS(R.string.every_n_days, 6),
    EVERY_WEEK(R.string.every_week, 7),
    EVERY_2_WEEKS(R.string.every_n_weeks, 14),
    EVERY_3_WEEKS(R.string.every_n_weeks, 21),
    EVERY_MONTH(R.string.every_month, 30);

    companion object {
        fun fromDays(days: Int): Frequency = when (days) {
            1 -> EVERYDAY
            2 -> EVERY_2_DAYS
            3 -> EVERY_3_DAYS
            4 -> EVERY_4_DAYS
            5 -> EVERY_5_DAYS
            6 -> EVERY_6_DAYS
            7 -> EVERY_WEEK
            14 -> EVERY_2_WEEKS
            21 -> EVERY_3_WEEKS
            30 -> EVERY_MONTH
            else -> EVERYDAY
        }
    }
}

fun getFrequencyList(): List<Frequency> = listOf(
    Frequency.EVERYDAY,
    Frequency.EVERY_2_DAYS,
    Frequency.EVERY_3_DAYS,
    Frequency.EVERY_4_DAYS,
    Frequency.EVERY_5_DAYS,
    Frequency.EVERY_6_DAYS,
    Frequency.EVERY_WEEK,
    Frequency.EVERY_2_WEEKS,
    Frequency.EVERY_3_WEEKS,
    Frequency.EVERY_MONTH
)
