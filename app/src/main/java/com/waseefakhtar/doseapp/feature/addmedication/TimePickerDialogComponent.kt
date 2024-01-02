package com.waseefakhtar.doseapp.feature.addmedication

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.time.LocalTime

@Composable
fun TimePickerDialogComponent(
    showDialog: Boolean,
    selectedTime: LocalTime,
    onSelectedTime: (LocalTime) -> Unit
) {
    val listener = setUpOnTimeSetListener(onSelectedTime)
    val timePickerDialog = getTimePickerDialog(selectedTime, listener)
    if (showDialog) {
        timePickerDialog.show()
    }
}

private fun setUpOnTimeSetListener(
    onSelectedTime: (LocalTime) -> Unit
): TimePickerDialog.OnTimeSetListener {
    return TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        val time = LocalTime.of(hourOfDay, minute)
        onSelectedTime(time)
    }
}

@Composable
private fun getTimePickerDialog(
    selectedTime: LocalTime,
    listener: TimePickerDialog.OnTimeSetListener,
    context: Context = LocalContext.current
): TimePickerDialog {
    return TimePickerDialog(context, listener, selectedTime.hour, selectedTime.minute, false)
}
