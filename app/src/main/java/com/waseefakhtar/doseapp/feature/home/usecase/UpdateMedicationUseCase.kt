package com.waseefakhtar.doseapp.feature.home.usecase

import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.domain.repository.MedicationRepository
import javax.inject.Inject

class UpdateMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {

    suspend fun deleteMedication(medication: Medication){
        return repository.deleteMedication(medication)
    }
    suspend fun updateMedication(medication: Medication) {
        return repository.updateMedication(medication)
    }
}
