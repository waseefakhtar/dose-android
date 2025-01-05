package com.waseefakhtar.doseapp.data.repository

import com.waseefakhtar.doseapp.data.MedicationDao
import com.waseefakhtar.doseapp.data.mapper.toMedication
import com.waseefakhtar.doseapp.data.mapper.toMedicationEntity
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.domain.repository.MedicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MedicationRepositoryImpl(
    private val dao: MedicationDao
) : MedicationRepository {

    override suspend fun insertMedications(medications: List<Medication>): Flow<List<Medication>> = flow {
        val savedIds = medications.map { medication ->
            dao.insertMedication(medication.toMedicationEntity())
        }
        // Get the saved medications with their IDs
        val savedMedications = medications.mapIndexed { index, medication ->
            medication.copy(id = savedIds[index])
        }
        emit(savedMedications)
    }

    override suspend fun deleteMedication(medication: Medication) {
        dao.deleteMedication(medication.toMedicationEntity())
    }

    override suspend fun updateMedication(medication: Medication) {
        dao.updateMedication(medication.toMedicationEntity())
    }

    override fun getAllMedications(): Flow<List<Medication>> {
        return dao.getAllMedications().map { entities ->
            entities.map { it.toMedication() }
        }
    }

    override fun getMedicationsForDate(date: String): Flow<List<Medication>> {
        return dao.getMedicationsForDate(
            date = date
        ).map { entities ->
            entities.map { it.toMedication() }
        }
    }

    override suspend fun getMedicationById(id: Long): Medication? {
        return dao.getMedicationById(id)?.toMedication()
    }
}
