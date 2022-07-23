package com.waseefakhtar.doseapp.data.entity

import androidx.room.PrimaryKey
import com.waseefakhtar.doseapp.util.TimesOfDay
import java.util.*

data class MedicationEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val dosage: Int,
    val recurrence: String,
    val endDate: Date,
    val timesOfDay: List<TimesOfDay>,
)