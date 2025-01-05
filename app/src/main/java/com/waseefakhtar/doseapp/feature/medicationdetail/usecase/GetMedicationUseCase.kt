package com.waseefakhtar.doseapp.feature.medicationdetail.usecase

import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.domain.repository.MedicationRepository
import javax.inject.Inject

class GetMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(id: Long): Medication? = repository.getMedicationById(id)
}
