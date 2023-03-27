package com.waseefakhtar.doseapp.domain.model

import android.os.Parcelable
import com.waseefakhtar.doseapp.util.TimesOfDay
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Medication(
    val id: Long?,
    val name: String,
    val dosage: Int,
    val recurrence: String,
    val endDate: Date,
    val timesOfDay: List<TimesOfDay>,
    val medicationTaken: Boolean,
    val date: Date
) : Parcelable
