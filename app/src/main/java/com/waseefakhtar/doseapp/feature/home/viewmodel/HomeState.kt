package com.waseefakhtar.doseapp.feature.home.viewmodel

import com.waseefakhtar.doseapp.domain.model.Medication

data class HomeState(
    val greeting: String = "",
    val userName: String = "",
    val lastSelectedDate: String,
    val medications: List<Medication> = emptyList()
)
