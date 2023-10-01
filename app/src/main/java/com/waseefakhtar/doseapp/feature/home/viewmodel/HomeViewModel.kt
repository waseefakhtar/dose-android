package com.waseefakhtar.doseapp.feature.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.feature.home.usecase.DeleteMedicationUseCase
import com.waseefakhtar.doseapp.feature.home.usecase.GetMedicationsUseCase
import com.waseefakhtar.doseapp.feature.home.usecase.UpdateMedicationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMedicationsUseCase: GetMedicationsUseCase,
    private val updateMedicationUseCase: UpdateMedicationUseCase,
    private val deleteMedicationUseCase: DeleteMedicationUseCase
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    init {
        loadMedications()
    }



    fun getUserName() {
        state = state.copy(userName = "Kathryn")
        // TODO: Get user name from DB
    }

    fun getGreeting() {
        state = state.copy(greeting = "Greeting")
        // TODO: Get greeting by checking system time
    }

    fun loadMedications() {
        viewModelScope.launch {
            getMedicationsUseCase.getMedications().onEach { medicationList ->
                state = state.copy(
                    medications = medicationList
                )
            }.launchIn(viewModelScope)
        }
    }

    fun takeMedication(medication: Medication) {
        viewModelScope.launch {
            updateMedicationUseCase.updateMedication(medication)
        }
    }

    fun getUserPlan() {
        // TODO: Get user plan
    }

    fun showDialog(medication: Medication){
        state = state.copy(isDeleteDialogShown = true, medicationToDelete = medication)
    }

    fun closeDialog(){
        state = state.copy(isDeleteDialogShown = false, medicationToDelete = null)
    }

    fun deleteMedication(medication: Medication?){
        state = state.copy(isDeleteDialogShown = false, medicationToDelete = null)

        viewModelScope.launch {
            medication?.let {
                deleteMedicationUseCase(it)
            }
        }
    }
}
