package com.waseefakhtar.doseapp.data.mapper

import com.waseefakhtar.doseapp.data.entity.MedicationEntity
import com.waseefakhtar.doseapp.domain.model.Medication
import java.time.LocalTime

fun MedicationEntity.toMedication(): Medication {
    return Medication(
        id = id,
        name = name,
        dosage = dosage,
        recurrence = recurrence,
        endDate = endDate.toLocalDate(),
        medicationTime = medicationTime,
        medicationTaken = medicationTaken
    )
}

fun Medication.toMedicationEntity(): MedicationEntity {
    return MedicationEntity(
        id = id ?: 0L,
        name = name,
        dosage = dosage,
        recurrence = recurrence,
        endDate = endDate.atTime(LocalTime.MIN),
        medicationTime = medicationTime,
        medicationTaken = medicationTaken
    )
}
