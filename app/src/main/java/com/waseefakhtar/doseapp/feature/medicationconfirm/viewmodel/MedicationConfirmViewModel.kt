package com.waseefakhtar.doseapp.feature.medicationconfirm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waseefakhtar.doseapp.feature.medicationconfirm.usecase.AddMedicationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicationConfirmViewModel @Inject constructor(
    private val addMedicationUseCase: AddMedicationUseCase
) : ViewModel() {

    fun addMedication(state: MedicationConfirmState) {
        viewModelScope.launch {
            val medication = state.medication
            addMedicationUseCase.addMedication(medication)
        }
    }
}
