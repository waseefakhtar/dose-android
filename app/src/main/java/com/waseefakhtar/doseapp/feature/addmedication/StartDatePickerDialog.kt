package com.waseefakhtar.doseapp.feature.addmedication

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.waseefakhtar.doseapp.R
import com.waseefakhtar.doseapp.extension.toFormattedDateString
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartDatePickerDialog(
    showDialog: Boolean,
    selectedDate: Long?,
    onDismiss: () -> Unit,
    onDateSelected: (startDate: Long) -> Unit,
) {
    if (showDialog) {
        val today = Calendar.getInstance().timeInMillis
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = if (selectedDate == 0L) today else selectedDate,
        )

        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    enabled = datePickerState.selectedDateMillis != null,
                    onClick = {
                        datePickerState.selectedDateMillis?.let { start ->
                            onDateSelected(start)
                        }
                        onDismiss()
                    },
                ) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.cancel))
                }
            },
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false,
                title = {
                    Text(
                        text = stringResource(R.string.select_start_date),
                        modifier = Modifier.padding(
                            start = 24.dp,
                            end = 12.dp,
                            top = 16.dp,
                        ),
                    )
                },
                headline = {
                    datePickerState.selectedDateMillis?.toFormattedDateString()?.let {
                        Text(
                            modifier = Modifier.padding(
                                start = 24.dp,
                                end = 12.dp,
                                bottom = 12.dp,
                            ),
                            text = it
                        )
                    }
                },
            )
        }
    }
}
