package com.waseefakhtar.doseapp.feature.history.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waseefakhtar.doseapp.analytics.AnalyticsHelper
import com.waseefakhtar.doseapp.feature.home.usecase.GetMedicationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getMedicationsUseCase: GetMedicationsUseCase,
    private val analyticsHelper: AnalyticsHelper
) : ViewModel() {

    var state by mutableStateOf(HistoryState())
        private set

    init {
        loadMedications()
    }

    private fun loadMedications() {
        viewModelScope.launch {
            getMedicationsUseCase.getMedications().onEach { medicationList ->
                state = state.copy(
                    medications = medicationList
                )
            }.launchIn(viewModelScope)
        }
    }

    fun logEvent(eventName: String) {
        analyticsHelper.logEvent(eventName = eventName)
    }
}
