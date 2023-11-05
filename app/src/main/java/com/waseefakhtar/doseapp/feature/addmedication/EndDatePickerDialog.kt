package com.waseefakhtar.doseapp.feature.addmedication

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.waseefakhtar.doseapp.R
import com.waseefakhtar.doseapp.extension.toFormattedDateString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndDatePickerDialog(
    state: DatePickerState,
    shouldDisplay: Boolean,
    onConfirmClicked: (selectedDateInMillis: Long) -> Unit,
    dismissRequest: () -> Unit
) {
    if (shouldDisplay) {
        DatePickerDialog(
            onDismissRequest = dismissRequest,
            confirmButton = {
                Button(
                    modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp),
                    onClick = {
                        state.selectedDateMillis?.let {
                            onConfirmClicked(it)
                        }
                        dismissRequest()
                    }
                ) {
                    Text(text = stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = dismissRequest) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
            content = {
                DatePicker(
                    state = state,
                    showModeToggle = false,
                    headline = {
                        state.selectedDateMillis?.toFormattedDateString()?.let {
                            Text(
                                modifier = Modifier.padding(start = 16.dp),
                                text = it
                            )
                        }
                    }
                )
            }
        )
    }
}
