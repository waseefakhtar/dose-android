package com.waseefakhtar.doseapp.util

enum class MedicationType {
    TABLET,
    CAPSULE,
    SYRUP,
    DROPS,
    SPRAY,
    GEL;

    companion object {
        fun getDefault() = TABLET
    }
}

fun getMedicationTypes(): List<MedicationType> = MedicationType.values().toList()
