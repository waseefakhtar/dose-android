package com.waseefakhtar.doseapp.feature.addmedication

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.waseefakhtar.doseapp.R
import com.waseefakhtar.doseapp.extension.formatDuration
import com.waseefakhtar.doseapp.util.formatDurationText
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    showDialog: Boolean,
    startDate: Long?,
    endDate: Long?,
    onDismiss: () -> Unit,
    onDateSelected: (startDate: Long, endDate: Long) -> Unit,
) {
    if (showDialog) {
        val today = Calendar.getInstance().timeInMillis
        val dateRangePickerState =
            rememberDateRangePickerState(
                initialSelectedStartDateMillis = if (startDate == 0L) null else startDate,
                initialSelectedEndDateMillis = if (endDate == 0L) null else endDate,
                initialDisplayedMonthMillis = today, // Show current month by default
            )

        LaunchedEffect(
            dateRangePickerState.selectedStartDateMillis,
            dateRangePickerState.selectedEndDateMillis,
        ) {
            if (
                dateRangePickerState.selectedStartDateMillis != null &&
                dateRangePickerState.selectedEndDateMillis == null
            ) {
                val newDate = dateRangePickerState.selectedStartDateMillis!!
                if (endDate != null && endDate != 0L && newDate > endDate) {
                    dateRangePickerState.setSelection(startDate!!, newDate)
                }
            }
        }

        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    enabled =
                    dateRangePickerState.selectedStartDateMillis != null &&
                        dateRangePickerState.selectedEndDateMillis != null,
                    onClick = {
                        dateRangePickerState.selectedStartDateMillis?.let { start ->
                            dateRangePickerState.selectedEndDateMillis?.let { end ->
                                onDateSelected(start, end)
                            }
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
            DateRangePicker(
                state = dateRangePickerState,
                title = {
                    Text(
                        text = stringResource(R.string.select_duration),
                        modifier =
                        Modifier.padding(
                            start = 24.dp,
                            end = 12.dp,
                            top = 16.dp,
                        ),
                    )
                },
                headline = {
                    if (
                        dateRangePickerState.selectedStartDateMillis != null &&
                        dateRangePickerState.selectedEndDateMillis != null
                    ) {
                        val duration =
                            dateRangePickerState.selectedStartDateMillis!!
                                .formatDuration(dateRangePickerState.selectedEndDateMillis!!)
                        Text(
                            text = formatDurationText(duration),
                            style = MaterialTheme.typography.headlineSmall,
                            modifier =
                            Modifier
                                .padding(
                                    start = 24.dp,
                                    end = 12.dp,
                                    bottom = 12.dp,
                                ).fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
            )
        }
    }
}
