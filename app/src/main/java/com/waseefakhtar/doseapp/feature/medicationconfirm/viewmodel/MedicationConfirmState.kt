package com.waseefakhtar.doseapp.feature.medicationconfirm.viewmodel

import com.waseefakhtar.doseapp.domain.model.Medication

data class MedicationConfirmState(
    val medications: List<Medication>
)
