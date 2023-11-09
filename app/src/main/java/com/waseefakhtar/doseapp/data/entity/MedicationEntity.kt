package com.waseefakhtar.doseapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val dosage: Int,
    val recurrence: String,
    val endDate: Date,
    val medicationTime: Date = Date(),
    val medicationTaken: Boolean,
)
