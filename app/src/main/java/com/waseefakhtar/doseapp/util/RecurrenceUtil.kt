package com.waseefakhtar.doseapp.util

enum class Recurrence(val recurrenceString: String) {
    Daily("Daily"),
    Weekly("Weekly"),
    BiWeekly("Biweekly"),
    Monthly("Monthly")
}

fun getRecurrenceList(): List<Recurrence> {
    val recurrenceList = mutableListOf<Recurrence>()
    recurrenceList.add(Recurrence.Daily)
    recurrenceList.add(Recurrence.Weekly)
    recurrenceList.add(Recurrence.BiWeekly)
    recurrenceList.add(Recurrence.Monthly)

    return recurrenceList
}
