package com.waseefakhtar.doseapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val dosage: Int,
    val recurrence: String,
    val endDate: LocalDateTime,
    val medicationTime: LocalDateTime = LocalDateTime.now(),
    val medicationTaken: Boolean,
)
