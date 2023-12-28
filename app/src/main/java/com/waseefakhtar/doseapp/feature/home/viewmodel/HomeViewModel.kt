package com.waseefakhtar.doseapp.feature.home.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.extension.toFormattedYeahMonthDateString
import com.waseefakhtar.doseapp.feature.home.usecase.GetMedicationsUseCase
import com.waseefakhtar.doseapp.feature.home.usecase.UpdateMedicationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMedicationsUseCase: GetMedicationsUseCase,
    private val updateMedicationUseCase: UpdateMedicationUseCase
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set
    private var dateFilter = MutableStateFlow(Date().toFormattedYeahMonthDateString())

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

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadMedications() {
        viewModelScope.launch {
            dateFilter.flatMapLatest { selectedDate ->
                getMedicationsUseCase.getMedications(selectedDate).onEach { medicationList ->
                    state = state.copy(
                        medications = medicationList
                    )
                }
            }.launchIn(viewModelScope)
        }
    }

    fun selectDate(date: Date) {
        dateFilter.update { date.toFormattedYeahMonthDateString() }
    }

    fun takeMedication(medication: Medication) {
        viewModelScope.launch {
            updateMedicationUseCase.updateMedication(medication)
        }
    }

    fun getUserPlan() {
        // TODO: Get user plan
    }
}
