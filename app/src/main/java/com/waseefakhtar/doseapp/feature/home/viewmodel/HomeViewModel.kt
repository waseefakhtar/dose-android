package com.waseefakhtar.doseapp.feature.home.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waseefakhtar.doseapp.analytics.AnalyticsHelper
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.extension.toFormattedYearMonthDateString
import com.waseefakhtar.doseapp.feature.home.model.CalendarModel
import com.waseefakhtar.doseapp.feature.home.usecase.GetMedicationsUseCase
import com.waseefakhtar.doseapp.feature.home.usecase.UpdateMedicationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMedicationsUseCase: GetMedicationsUseCase,
    private val updateMedicationUseCase: UpdateMedicationUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val analyticsHelper: AnalyticsHelper
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(Date())
    private val _dateFilter = savedStateHandle.getStateFlow(
        DATE_FILTER_KEY,
        Date().toFormattedYearMonthDateString()
    )
    private val _greeting = MutableStateFlow("")
    private val _userName = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _medications = _dateFilter.flatMapLatest { selectedDate ->
        getMedicationsUseCase.getMedications(selectedDate)
    }

    val homeUiState = combine(
        _selectedDate,
        _medications,
        _dateFilter,
        _greeting,
        _userName
    ) { selectedDate, medications, dateFilter, greeting, userName ->
        HomeState(
            lastSelectedDate = dateFilter,
            medications = medications.sortedBy { it.medicationTime },
            greeting = greeting,
            userName = userName
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HomeState(lastSelectedDate = Date().toFormattedYearMonthDateString())
    )

    fun updateSelectedDate(date: Date) {
        _selectedDate.value = date
        // Update the date filter to trigger new medication fetch
        savedStateHandle[DATE_FILTER_KEY] = date.toFormattedYearMonthDateString()
    }

    init {
        getUserName()
        getGreeting()
    }

    private fun getUserName() {
        _userName.value = "Kathryn"
        // TODO: Get user name from DB
    }

    private fun getGreeting() {
        _greeting.value = "Greeting"
        // TODO: Get greeting by checking system time
    }

    fun selectDate(selectedDate: CalendarModel.DateModel) {
        savedStateHandle[DATE_FILTER_KEY] = selectedDate.date.toFormattedYearMonthDateString()
    }

    fun takeMedication(medication: Medication) {
        viewModelScope.launch {
            updateMedicationUseCase.updateMedication(medication)
        }
    }

    fun getUserPlan() {
        // TODO: Get user plan
    }

    fun logEvent(eventName: String) {
        analyticsHelper.logEvent(eventName = eventName)
    }

    companion object {
        const val DATE_FILTER_KEY = "medication_date_filter"
    }
}
