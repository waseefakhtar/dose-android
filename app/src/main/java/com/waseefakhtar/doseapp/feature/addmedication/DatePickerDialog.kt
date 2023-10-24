package com.waseefakhtar.doseapp.feature.addmedication

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.waseefakhtar.doseapp.R
import com.waseefakhtar.doseapp.extension.dateInMillisToFormattedString


/**
 * Created by MJ Jacobs on 2023/10/24 at 04:39
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
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
                    onClick = {
                        state.selectedDateMillis?.let {
                            onConfirmClicked(it)
                        }
                        dismissRequest()
                    }
                ) {
                    Text(text = stringResource(R.string.confirmButton))
                }
            },
            dismissButton = {
                OutlinedButton(onClick = dismissRequest) {
                    Text(text = stringResource(R.string.dismissButton))
                }
            },
            content = {
                DatePicker(
                    state = state,
                    showModeToggle = false,
                    headline = {
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = state.selectedDateMillis!!.dateInMillisToFormattedString()
                        )
                    }
                )
            }
        )
    }
}