package com.waseefakhtar.doseapp.domain.model

import android.os.Parcelable
import com.waseefakhtar.doseapp.util.TimesOfDay
import kotlinx.parcelize.Parcelize
import java.time.Instant

@Parcelize
data class Medication(
    val id: Long?,
    val name: String,
    val dosage: Int,
    val recurrence: String,
    val endDate: Instant,
    val timesOfDay: List<TimesOfDay>,
    val medicationTaken: Boolean,
    val date: Instant
) : Parcelable
