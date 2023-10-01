package com.waseefakhtar.doseapp.feature.home.usecase

import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.domain.repository.MedicationRepository
import javax.inject.Inject

class DeleteMedicationUseCase @Inject constructor(
    private val repository : MedicationRepository
) {

   suspend operator fun invoke(medication : Medication){
        repository.deleteMedication(medication)
    }
}