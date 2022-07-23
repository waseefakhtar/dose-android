package com.waseefakhtar.doseapp.domain.repository

import com.waseefakhtar.doseapp.data.MedicationDao
import com.waseefakhtar.doseapp.domain.model.Medication
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface MedicationRepository {

    suspend fun insertMedication(medication: Medication)

    suspend fun deleteMedication(medication: Medication)

    fun getMedicationsForDate(localDate: LocalDate): Flow<List<MedicationDao>>
}