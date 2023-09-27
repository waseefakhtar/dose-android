package com.waseefakhtar.doseapp.feature.medicationconfirm.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waseefakhtar.doseapp.MedicationNotificationService
import com.waseefakhtar.doseapp.feature.medicationconfirm.usecase.AddMedicationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicationConfirmViewModel @Inject constructor(
    private val addMedicationUseCase: AddMedicationUseCase
) : ViewModel() {

    private val _isMedicationSaved = MutableSharedFlow<Unit>()
    val isMedicationSaved = _isMedicationSaved.asSharedFlow()

    fun addMedication(context: Context, state: MedicationConfirmState) {
        viewModelScope.launch {
            val medications = state.medications
            val medicationAdded = addMedicationUseCase.addMedication(medications)

            for (medication in medications) {
                val service = MedicationNotificationService(context)
                service.scheduleNotification(medication)
            }

            _isMedicationSaved.emit(medicationAdded)
        }
    }
}
