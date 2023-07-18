package com.waseefakhtar.doseapp.feature.home.viewmodel

import com.waseefakhtar.doseapp.domain.model.Medication

data class HomeState(
    val greeting: String = "",
    val userName: String = "",
    val medications: List<Medication> = emptyList(),
    val isDeleteDialogShown : Boolean = false,
    val medicationToDelete : Medication? = null
)
