package com.waseefakhtar.doseapp.data.repository

import com.waseefakhtar.doseapp.data.MedicationDao
import com.waseefakhtar.doseapp.data.mapper.toMedication
import com.waseefakhtar.doseapp.data.mapper.toMedicationEntity
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.domain.repository.MedicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class MedicationRepositoryImpl(
    private val dao: MedicationDao
) : MedicationRepository {

    override suspend fun insertMedication(medication: Medication) {
        dao.insertMedication(medication.toMedicationEntity())
    }

    override suspend fun deleteMedication(medication: Medication) {
        dao.deleteMedication(medication.toMedicationEntity())
    }

    override fun getMedicationsForDate(date: Date): Flow<List<Medication>> {
        return dao.getFoodsForDate(
            date = date
        ).map { entities ->
            entities.map { it.toMedication() }
        }
    }
}
