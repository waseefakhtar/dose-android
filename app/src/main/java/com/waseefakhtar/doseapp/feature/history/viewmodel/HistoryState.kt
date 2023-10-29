package com.waseefakhtar.doseapp.feature.history.viewmodel

import com.waseefakhtar.doseapp.domain.model.Medication

data class HistoryState(
    val medications: List<Medication> = emptyList()
)
