package com.waseefakhtar.doseapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime

@Parcelize
data class Medication(
    val id: Long?,
    val name: String,
    val dosage: Int,
    val recurrence: String,
    val endDate: LocalDate,
    val medicationTaken: Boolean,
    val medicationTime: LocalDateTime,
) : Parcelable
