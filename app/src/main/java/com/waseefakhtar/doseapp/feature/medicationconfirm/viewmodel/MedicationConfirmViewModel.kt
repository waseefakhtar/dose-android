package com.waseefakhtar.doseapp.feature.medicationconfirm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waseefakhtar.doseapp.feature.medicationconfirm.usecase.AddMedicationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicationConfirmViewModel @Inject constructor(
    private val addMedicationUseCase: AddMedicationUseCase
) : ViewModel() {

    private val _isMedicationSaved = MutableSharedFlow<Long>()
    val isMedicationSaved = _isMedicationSaved.asSharedFlow()

    fun addMedication(state: MedicationConfirmState) {
        viewModelScope.launch {
            val medication = state.medication
            _isMedicationSaved.emit(addMedicationUseCase.addMedication(medication))
        }
    }
}
