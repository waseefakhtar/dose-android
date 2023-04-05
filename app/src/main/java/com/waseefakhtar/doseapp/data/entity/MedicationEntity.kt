package com.waseefakhtar.doseapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.waseefakhtar.doseapp.util.TimesOfDay
import java.time.Instant

@Entity
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val dosage: Int,
    val recurrence: String,
    val endDate: Instant,
    val timesOfDay: List<TimesOfDay>,
    val medicationTaken: Boolean,
    val date: Instant
)
