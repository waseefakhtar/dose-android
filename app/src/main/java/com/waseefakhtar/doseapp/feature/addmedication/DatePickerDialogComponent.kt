package com.waseefakhtar.doseapp.feature.addmedication

import android.app.DatePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.waseefakhtar.doseapp.feature.addmedication.model.CalendarInformation
import java.util.Calendar

@Composable
fun DatePickerDialogComponent(
    showDialog: Boolean,
    selectedDate: CalendarInformation,
    onSelectedDate: (selectedDate: CalendarInformation) -> Unit
) {
    val listener = setUpOnDateSetListener(onSelectedDate)
    val datePickerDialog = getDatePickerDialog(selectedDate, listener)
    if (showDialog) {
        datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
        datePickerDialog.show()
    }
}

private fun setUpOnDateSetListener(
    onSelectedDate: (selectedDate: CalendarInformation) -> Unit
): DatePickerDialog.OnDateSetListener {
    return DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
        val newDate = Calendar.getInstance().apply { set(selectedYear, selectedMonth, selectedDay) }
        onSelectedDate(CalendarInformation(newDate))
    }
}

@Composable
private fun getDatePickerDialog(
    selectedDate: CalendarInformation,
    listener: DatePickerDialog.OnDateSetListener
): DatePickerDialog {
    val context = LocalContext.current
    val (year, month, day) = selectedDate.dateInformation
    return DatePickerDialog(
        context,
        listener,
        year,
        month,
        day
    )
}
