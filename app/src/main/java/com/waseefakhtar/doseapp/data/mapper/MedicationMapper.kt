package com.waseefakhtar.doseapp.data.mapper

import com.waseefakhtar.doseapp.data.entity.MedicationEntity
import com.waseefakhtar.doseapp.domain.model.Medication

fun MedicationEntity.toMedication(): Medication {
    return Medication(
        name = name,
        dosage = dosage,
        recurrence = recurrence,
        endDate = endDate,
        timesOfDay = timesOfDay,
    )
}

fun Medication.toMedicationEntity(): MedicationEntity {
    return MedicationEntity(
        name = name,
        dosage = dosage,
        recurrence = recurrence,
        endDate = endDate,
        timesOfDay = timesOfDay
    )
}
