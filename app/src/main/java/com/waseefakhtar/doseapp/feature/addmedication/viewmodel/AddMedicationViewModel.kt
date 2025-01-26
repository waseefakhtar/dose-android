package com.waseefakhtar.doseapp.feature.addmedication.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.waseefakhtar.doseapp.analytics.AnalyticsHelper
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.feature.addmedication.model.CalendarInformation
import com.waseefakhtar.doseapp.util.Frequency
import com.waseefakhtar.doseapp.util.MedicationType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddMedicationViewModel @Inject constructor(
    private val analyticsHelper: AnalyticsHelper,
    @ApplicationContext private val context: Context
) : ViewModel() {

    fun createMedications(
        name: String,
        dosage: Int,
        frequency: String,
        startDate: Date,
        endDate: Date,
        medicationTimes: List<CalendarInformation>,
        type: MedicationType,
    ): List<Medication> {
        val frequencyValue = Frequency.valueOf(frequency)
        val interval = try {
            frequencyValue.days
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid frequency: $frequency")
        }

        val oneDayInMillis = 86400 * 1000 // Number of milliseconds in one day
        val durationInDays = ((endDate.time + oneDayInMillis - startDate.time) / oneDayInMillis).toInt()

        // Always create at least one occurrence if we have a valid duration
        val numOccurrences = if (durationInDays > 0) maxOf(1, durationInDays / interval) else 0

        // Create a Medication object for each occurrence and add it to a list
        val medications = mutableListOf<Medication>()
        val calendar = Calendar.getInstance()
        calendar.time = startDate

        val formattedFrequency = context.getString(frequencyValue.stringResId, frequencyValue.days)
        for (i in 0 until numOccurrences) {
            for (medicationTime in medicationTimes) {
                val medication = Medication(
                    id = 0,
                    name = name,
                    dosage = dosage,
                    frequency = formattedFrequency,
                    startDate = startDate,
                    endDate = endDate,
                    medicationTaken = false,
                    medicationTime = getMedicationTime(medicationTime, calendar),
                    type = type
                )
                medications.add(medication)
            }

            // Increment the date based on the frequency interval
            calendar.add(Calendar.DAY_OF_YEAR, interval)
        }

        return medications
    }

    private fun getMedicationTime(medicationTime: CalendarInformation, calendar: Calendar): Date {
        calendar.set(Calendar.HOUR_OF_DAY, medicationTime.dateInformation.hour)
        calendar.set(Calendar.MINUTE, medicationTime.dateInformation.minute)
        return calendar.time
    }

    fun logEvent(eventName: String) {
        analyticsHelper.logEvent(eventName = eventName)
    }
}
