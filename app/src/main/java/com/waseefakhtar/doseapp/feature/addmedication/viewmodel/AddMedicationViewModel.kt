package com.waseefakhtar.doseapp.feature.addmedication.viewmodel

import androidx.lifecycle.ViewModel
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.util.Recurrence
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Period

class AddMedicationViewModel : ViewModel() {

    fun createMedications(
        name: String,
        dosage: Int,
        recurrence: Recurrence,
        endDate: LocalDate,
        medicationTimes: List<LocalTime>,
        startDate: LocalDate = LocalDate.now()
    ): List<Medication> {

        // Determine the recurrence interval based on the selected recurrence
        val interval = when (recurrence) {
            Recurrence.Daily -> 1
            Recurrence.Weekly -> 7
            Recurrence.Monthly -> 30
        }

        val daysDifference = Period.between(startDate, endDate).days

        val numOccurrences = daysDifference / interval + 1

        // Create a Medication object for each occurrence and add it to a list
        val medications = mutableListOf<Medication>()
        var start = startDate
        repeat(numOccurrences) {
            medicationTimes.forEach { time ->
                val medicationDateTime = LocalDateTime.of(start, time)
                // TODO: Generate id automatically.
                val medication = Medication(
                    id = 0,
                    name = name,
                    dosage = dosage,
                    recurrence = recurrence.name,
                    endDate = endDate,
                    medicationTaken = false,
                    medicationTime = medicationDateTime
                )
                medications.add(medication)
            }
            // Increment the date based on the recurrence interval
            start = start.plusDays(interval.toLong())
        }

        return medications
    }

}
