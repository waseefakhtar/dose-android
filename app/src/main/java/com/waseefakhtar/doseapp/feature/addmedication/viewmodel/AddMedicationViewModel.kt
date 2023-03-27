package com.waseefakhtar.doseapp.feature.addmedication.viewmodel

import androidx.lifecycle.ViewModel
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.util.TimesOfDay
import java.util.Calendar
import java.util.Date

class AddMedicationViewModel : ViewModel() {

    fun createMedications(
        name: String,
        dosage: Int,
        recurrence: String,
        endDate: Date,
        timesOfDay: List<TimesOfDay>,
        startDate: Date = Date()
    ): List<Medication> {

        // Determine the recurrence interval based on the selected recurrence
        val interval = when (recurrence) {
            "Daily" -> 1
            "Weekly" -> 7
            "Monthly" -> 30
            else -> throw IllegalArgumentException("Invalid recurrence: $recurrence")
        }

        val oneDayInMillis = 86400 * 1000 // Number of milliseconds in one day
        val numOccurrences = ((endDate.time + oneDayInMillis - startDate.time) / (interval * oneDayInMillis)).toInt() + 1

        // Create a Medication object for each occurrence and add it to a list
        val medications = mutableListOf<Medication>()
        val calendar = Calendar.getInstance()
        calendar.time = startDate
        for (i in 0 until numOccurrences) {
            for (timeOfDay in timesOfDay) {
                val medication = Medication(
                    id = 0,
                    name = name,
                    dosage = dosage,
                    recurrence = recurrence,
                    endDate = endDate,
                    timesOfDay = listOf(timeOfDay),
                    medicationTaken = false,
                    date = calendar.time
                )
                medications.add(medication)
            }

            // Increment the date based on the recurrence interval
            calendar.add(Calendar.DAY_OF_YEAR, interval)
        }

        return medications
    }
}
