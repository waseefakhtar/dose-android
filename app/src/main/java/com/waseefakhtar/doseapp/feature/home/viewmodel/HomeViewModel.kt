package com.waseefakhtar.doseapp.feature.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.feature.medicationconfirm.usecase.AddMedicationUseCase
import com.waseefakhtar.doseapp.feature.medicationconfirm.viewmodel.MedicationConfirmState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val addMedicationUseCase: AddMedicationUseCase
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    fun getUserName() {
        state = state.copy(userName = "Kathryn")
        //TODO: Get user name from DB
    }

    fun getGreeting() {
        state = state.copy(greeting = "Greeting")
        //TODO: Get greeting by checking system time
    }

    fun getMedications() {
        //TODO: Get medications from DB
    }

    fun takeMedication(medication: Medication) {
        //TODO: Take medication and update DB
    }

    fun getUserPlan() {
        //TODO: Get user plan
    }
}