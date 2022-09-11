package com.waseefakhtar.doseapp.feature.home.usecase

import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.domain.repository.MedicationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMedicationsUseCase @Inject constructor(
    private val repository: MedicationRepository
) {

    suspend fun getMedications(): Flow<List<Medication>> {
        return repository.getAllMedications()
    }
}
