package com.waseefakhtar.doseapp.feature.medicationconfirm.usecase

import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.domain.repository.MedicationRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class AddMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend fun addMedication(medications: List<Medication>): Flow<List<Medication>> {
        return repository.insertMedications(medications)
    }
}
