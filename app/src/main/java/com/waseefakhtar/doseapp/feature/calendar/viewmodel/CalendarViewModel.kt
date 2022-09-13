package com.waseefakhtar.doseapp.feature.calendar.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf(CalendarState())
        private set

    // TODO: Fill out when Calendar feature is implemented
}
