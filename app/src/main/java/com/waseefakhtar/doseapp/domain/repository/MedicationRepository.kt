package com.waseefakhtar.doseapp.domain.repository

import com.waseefakhtar.doseapp.domain.model.Medication
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface MedicationRepository {

    suspend fun insertMedication(medication: Medication)

    suspend fun deleteMedication(medication: Medication)

    fun getAllMedications(): Flow<List<Medication>>

    fun getMedicationsForDate(date: Date): Flow<List<Medication>>
}
