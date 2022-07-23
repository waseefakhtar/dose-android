package com.waseefakhtar.doseapp.data.repository

import com.waseefakhtar.doseapp.data.MedicationDao
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.domain.repository.MedicationRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class MedicationRepositoryImpl(
    private val dao: MedicationDao
): MedicationRepository {

    override suspend fun insertMedication(medication: Medication) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMedication(medication: Medication) {
        TODO("Not yet implemented")
    }

    override fun getMedicationsForDate(localDate: LocalDate): Flow<List<MedicationDao>> {
        TODO("Not yet implemented")
    }


}