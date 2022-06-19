package com.waseefakhtar.doseapp.feature.addmedication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.waseefakhtar.doseapp.R
import com.waseefakhtar.doseapp.util.Recurrence
import com.waseefakhtar.doseapp.util.getRecurrenceList
import java.util.*

@Composable
fun AddMedicationRoute(
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    //viewModel: CalendarViewModel = hiltViewModel()
) {
    AddMedicationScreen(onBackClicked)
}

@Composable
fun AddMedicationScreen(onBackClicked: () -> Unit) {
    var medicationName by rememberSaveable { mutableStateOf("") }


    Column(
        modifier = Modifier.padding(0.dp, 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FloatingActionButton(
            onClick = {
                onBackClicked()
            },
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }
        Text(
            text = stringResource(id = R.string.add_medication),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall
        )
        
        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = stringResource(id = R.string.medication_name),
            style = MaterialTheme.typography.bodyLarge
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = medicationName,
            onValueChange = { medicationName = it },
            //label = { Text(text = stringResource(id = R.string.medication_name)) },
            placeholder = { Text(text = "Hexamine") },
        )

        Spacer(modifier = Modifier.padding(4.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DosageTextField()
            RecurrenceDropdownMenu()
        }
    }
}

@Composable
fun DosageTextField() {
    var dose by rememberSaveable { mutableStateOf("") }
    var isMaxDoseError by rememberSaveable { mutableStateOf(false) }
    val maxDose = 3

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.dose_per_day),
            style = MaterialTheme.typography.bodyLarge
        )
        TextField(
            modifier = Modifier.width(128.dp),
            value = dose,
            onValueChange = {
                if (it.length < maxDose) {
                    isMaxDoseError = false
                    dose = it
                } else {
                    isMaxDoseError = true
                }
            },
            trailingIcon = {
                if (isMaxDoseError) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
            placeholder = { Text(text = "3") },
            isError = isMaxDoseError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        if (isMaxDoseError) {
            Text(
                text = "You cannot have more than 99 dosage per day.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurrenceDropdownMenu() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.recurrence),
            style = MaterialTheme.typography.bodyLarge
        )

        val options = getRecurrenceList().map { it.recurrenceString }
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionText by remember { mutableStateOf(options[0]) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                readOnly = true,
                value = selectedOptionText,
                onValueChange = {},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RecurrencePickerView(
    selectedRecurrence: Recurrence,
    onSelection: (Recurrence) -> Unit,
    recurrenceList: List<Recurrence>
) {
    var showDialog by remember { mutableStateOf(false) }
    Text(
        modifier = Modifier
            .clickable {
                showDialog = true
            }
            .padding(start = 20.dp, end = 5.dp),
        text = selectedRecurrence.recurrenceString
    )

    if (showDialog)
        RecurrencePickerDialog(recurrenceList, onSelection) {
            showDialog = false
        }
}

@Composable
fun RecurrencePickerDialog(
    recurrenceList: List<Recurrence>,
    onSelection: (Recurrence) -> Unit,
    dismiss: () -> Unit,
) {
    Dialog(onDismissRequest = dismiss) {
        Box {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 40.dp)
            ) {
                for (recurrence in recurrenceList) {
                    item {
                        Text(
                            modifier = Modifier
                                .clickable {
                                    onSelection(recurrence)
                                    dismiss()
                                }
                                .fillMaxWidth()
                                .padding(10.dp),
                            text = recurrence.recurrenceString
                        )
                    }
                }
            }
        }
    }
}