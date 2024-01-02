package com.waseefakhtar.doseapp.domain.repository

import com.waseefakhtar.doseapp.domain.model.Medication
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface MedicationRepository {

    suspend fun insertMedications(medications: List<Medication>)

    suspend fun deleteMedication(medication: Medication)

    suspend fun updateMedication(medication: Medication)

    fun getAllMedications(): Flow<List<Medication>>

    fun getMedicationsForDate(date: LocalDateTime): Flow<List<Medication>>
}
