package com.waseefakhtar.doseapp.feature.addmedication

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.waseefakhtar.doseapp.R
import com.waseefakhtar.doseapp.util.TimesOfDay
import com.waseefakhtar.doseapp.util.getRecurrenceList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddMedicationRoute(
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    //viewModel: CalendarViewModel = hiltViewModel()
) {
    AddMedicationScreen(onBackClicked)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreen(onBackClicked: () -> Unit) {
    var medicationName by rememberSaveable { mutableStateOf("") }
    var numberOfDosageSaveable by rememberSaveable { mutableStateOf("") }

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
                    value = numberOfDosageSaveable,
                    onValueChange = {
                        if (it.length < maxDose) {
                            isMaxDoseError = false
                            numberOfDosageSaveable = it
                        } else {
                            isMaxDoseError = true
                        }
                    },
                    trailingIcon = {
                        if (isMaxDoseError) {
                            Icon(
                                imageVector = Icons.Filled.Info,
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
            RecurrenceDropdownMenu()
        }

        Spacer(modifier = Modifier.padding(4.dp))
        UntilTextField()


        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = stringResource(id = R.string.times_of_day),
            style = MaterialTheme.typography.bodyLarge
        )

        var morningSelectionSaveable by rememberSaveable { mutableStateOf(false) }
        var afternoonSelectionSaveable by rememberSaveable { mutableStateOf(false) }
        var eveningSelectionSaveable by rememberSaveable { mutableStateOf(false) }
        var nightSelectionSaveable by rememberSaveable { mutableStateOf(false) }
        var selectionCount by rememberSaveable { mutableStateOf(0) }
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                selected = morningSelectionSaveable,
                onClick = {
                    if (shouldSelect(selectionCount, morningSelectionSaveable, afternoonSelectionSaveable, eveningSelectionSaveable, nightSelectionSaveable, numberOfDosageSaveable.toIntOrNull() ?: 0)) {
                        selectionCount++
                        morningSelectionSaveable = !morningSelectionSaveable
                    } else {
                        showMaxSelectionSnackbar(scope, numberOfDosageSaveable, context)
                    }
                },
                label = { Text(text = TimesOfDay.Morning.name)  },
                selectedIcon = { Icon(imageVector = Icons.Default.Done, contentDescription = "Selected") }
            )
            FilterChip(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                selected = afternoonSelectionSaveable,
                onClick = {
                    if (shouldSelect(selectionCount, morningSelectionSaveable, afternoonSelectionSaveable, eveningSelectionSaveable, nightSelectionSaveable, numberOfDosageSaveable.toIntOrNull() ?: 0)) {
                        selectionCount++
                        afternoonSelectionSaveable = !afternoonSelectionSaveable
                    } else {
                        showMaxSelectionSnackbar(scope, numberOfDosageSaveable, context)
                    }
                },
                label = { Text(text = TimesOfDay.Afternoon.name)  },
                selectedIcon = { Icon(imageVector = Icons.Default.Done, contentDescription = "Selected") }
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                selected = eveningSelectionSaveable,
                onClick = {
                    if (shouldSelect(selectionCount, morningSelectionSaveable, afternoonSelectionSaveable, eveningSelectionSaveable, nightSelectionSaveable, numberOfDosageSaveable.toIntOrNull() ?: 0)) {
                        selectionCount++
                        eveningSelectionSaveable = !eveningSelectionSaveable
                    } else {
                        showMaxSelectionSnackbar(scope, numberOfDosageSaveable, context)
                    }
                },
                label = { Text(text = TimesOfDay.Evening.name)  },
                selectedIcon = { Icon(imageVector = Icons.Default.Done, contentDescription = "Selected") }
            )
            FilterChip(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                selected = nightSelectionSaveable,
                onClick = {
                    if (shouldSelect(selectionCount, morningSelectionSaveable, afternoonSelectionSaveable, eveningSelectionSaveable, nightSelectionSaveable, numberOfDosageSaveable.toIntOrNull() ?: 0)) {
                        selectionCount++
                        nightSelectionSaveable = !nightSelectionSaveable
                    } else {
                        showMaxSelectionSnackbar(scope, numberOfDosageSaveable, context)
                    }
                },
                label = { Text(text = TimesOfDay.Night.name)  },
                selectedIcon = { Icon(imageVector = Icons.Default.Done, contentDescription = "Selected") }
            )
        }
    }
}

private fun shouldSelect(selectionCount: Int, morningSelection: Boolean, afternoonSelection: Boolean, eveningSelection: Boolean, nightSelection: Boolean, numberOfDosage: Int): Boolean {
    println("shouldSelect: ${selectionCount}. ${numberOfDosage}")

    return selectionCount < numberOfDosage
}

private fun showMaxSelectionSnackbar(scope: CoroutineScope, numberOfDosage: String, context: Context) {
    scope.launch {
        // TODO: Fix showing Snackbar.
        //SnackbarHostState().showSnackbar("You can only select ${numberOfDosage} times of days.")
    }
    Toast.makeText(context, "You're selecting ${(numberOfDosage.toIntOrNull() ?: 0) + 1} time(s) of days which is more than the number of dosage.", Toast.LENGTH_LONG).show()
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

        val options = getRecurrenceList().map { it.name }
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
fun UntilTextField() {
    Text(
        text = stringResource(id = R.string.until),
        style = MaterialTheme.typography.bodyLarge
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()

    val sdf = SimpleDateFormat("LLLL dd, yyyy", Locale.getDefault())
    val currentDate = sdf.format(Date())
    var selectedDate by rememberSaveable { mutableStateOf(currentDate) }


    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val year: Int = calendar.get(Calendar.YEAR)
    val month: Int = calendar.get(Calendar.MONTH)
    val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val mDatePickerDialog = DatePickerDialog(context, { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            selectedDate = "${month.toMonthName()} $dayOfMonth, $year"
        }, year, month, day)


    TextField(
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        value = selectedDate,
        onValueChange = {},
        trailingIcon = { Icons.Default.DateRange },
        interactionSource = interactionSource
    )

    if (isPressed) {
        mDatePickerDialog.show()
    }
}

fun Int.toMonthName(): String {
    return DateFormatSymbols().months[this]
}