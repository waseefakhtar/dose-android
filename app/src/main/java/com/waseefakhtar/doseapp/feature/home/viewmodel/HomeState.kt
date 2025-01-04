package com.waseefakhtar.doseapp.feature.home.viewmodel

import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.extension.toFormattedYearMonthDateString
import java.util.Date

data class HomeState(
    val greeting: String = "",
    val userName: String = "",
    val lastSelectedDate: String = Date().toFormattedYearMonthDateString(),
    val medications: List<Medication> = emptyList()
)
