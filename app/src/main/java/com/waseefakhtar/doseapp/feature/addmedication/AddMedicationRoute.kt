package com.waseefakhtar.doseapp.feature.addmedication

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waseefakhtar.doseapp.R
import com.waseefakhtar.doseapp.analytics.AnalyticsEvents
import com.waseefakhtar.doseapp.analytics.AnalyticsHelper
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.extension.dateInMillisToFormattedString
import com.waseefakhtar.doseapp.feature.addmedication.viewmodel.AddMedicationViewModel
import com.waseefakhtar.doseapp.util.Recurrence
import com.waseefakhtar.doseapp.util.TimesOfDay
import com.waseefakhtar.doseapp.util.getRecurrenceList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Date

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AddMedicationRoute(onBackClicked = {}, navigateToMedicationConfirm = {})
}

@Composable
fun AddMedicationRoute(
    onBackClicked: () -> Unit,
    navigateToMedicationConfirm: (List<Medication>) -> Unit,
    viewModel: AddMedicationViewModel = hiltViewModel()
) {
    val analyticsHelper = AnalyticsHelper.getInstance(LocalContext.current)
    AddMedicationScreen(onBackClicked, viewModel, analyticsHelper, navigateToMedicationConfirm)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreen(
    onBackClicked: () -> Unit,
    viewModel: AddMedicationViewModel,
    analyticsHelper: AnalyticsHelper,
    navigateToMedicationConfirm: (List<Medication>) -> Unit
) {
    var medicationName by rememberSaveable { mutableStateOf("") }
    var numberOfDosage by rememberSaveable { mutableStateOf("1") }
    var recurrence by rememberSaveable { mutableStateOf(Recurrence.Daily.name) }
    var endDate by rememberSaveable { mutableLongStateOf(Date().time) }
    var isMorningSelected by rememberSaveable { mutableStateOf(false) }
    var isAfternoonSelected by rememberSaveable { mutableStateOf(false) }
    var isEveningSelected by rememberSaveable { mutableStateOf(false) }
    var isNightSelected by rememberSaveable { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .padding(0.dp, 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FloatingActionButton(
                onClick = {
                    analyticsHelper.logEvent(AnalyticsEvents.ADD_MEDICATION_ON_BACK_CLICKED)
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
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = stringResource(id = R.string.medication_name),
            style = MaterialTheme.typography.bodyLarge
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = medicationName,
            onValueChange = { medicationName = it },
            // label = { Text(text = stringResource(id = R.string.medication_name)) },
            placeholder = { Text(text = "e.g. Risperdal, 4mg") },
        )

        Spacer(modifier = Modifier.padding(4.dp))

        var isMaxDoseError by rememberSaveable { mutableStateOf(false) }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                    value = numberOfDosage,
                    onValueChange = {
                        if (it.length < maxDose) {
                            isMaxDoseError = false
                            numberOfDosage = it
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
                    placeholder = { Text(text = "e.g. 1") },
                    isError = isMaxDoseError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            RecurrenceDropdownMenu { recurrence = it }
        }

        if (isMaxDoseError) {
            Text(
                text = "You cannot have more than 99 dosage per day.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        Spacer(modifier = Modifier.padding(4.dp))
        EndDateTextField { endDate = it }

        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = stringResource(id = R.string.times_of_day),
            style = MaterialTheme.typography.bodyLarge
        )

        var selectionCount by rememberSaveable { mutableIntStateOf(0) }
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                selected = isMorningSelected,
                onClick = {
                    handleSelection(
                        isSelected = isMorningSelected,
                        selectionCount = selectionCount,
                        canSelectMoreTimesOfDay = canSelectMoreTimesOfDay(
                            selectionCount,
                            numberOfDosage.toIntOrNull() ?: 0
                        ),
                        onStateChange = { count, selected ->
                            isMorningSelected = selected
                            selectionCount = count
                        },
                        onShowMaxSelectionError = {
                            showMaxSelectionSnackBar(scope, numberOfDosage, context)
                        }
                    )
                },
                label = { Text(text = TimesOfDay.Morning.name) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Selected"
                    )
                }
            )
            FilterChip(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                selected = isAfternoonSelected,
                onClick = {
                    handleSelection(
                        isSelected = isAfternoonSelected,
                        selectionCount = selectionCount,
                        canSelectMoreTimesOfDay = canSelectMoreTimesOfDay(
                            selectionCount,
                            numberOfDosage.toIntOrNull() ?: 0
                        ),
                        onStateChange = { count, selected ->
                            isAfternoonSelected = selected
                            selectionCount = count
                        },
                        onShowMaxSelectionError = {
                            showMaxSelectionSnackBar(scope, numberOfDosage, context)
                        }
                    )
                },
                label = { Text(text = TimesOfDay.Afternoon.name) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Selected"
                    )
                }
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                selected = isEveningSelected,
                onClick = {
                    handleSelection(
                        isSelected = isEveningSelected,
                        selectionCount = selectionCount,
                        canSelectMoreTimesOfDay = canSelectMoreTimesOfDay(
                            selectionCount,
                            numberOfDosage.toIntOrNull() ?: 0
                        ),
                        onStateChange = { count, selected ->
                            isEveningSelected = selected
                            selectionCount = count
                        },
                        onShowMaxSelectionError = {
                            showMaxSelectionSnackBar(scope, numberOfDosage, context)
                        }
                    )
                },
                label = { Text(text = TimesOfDay.Evening.name) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Selected"
                    )
                }
            )
            FilterChip(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                selected = isNightSelected,
                onClick = {
                    handleSelection(
                        isSelected = isNightSelected,
                        selectionCount = selectionCount,
                        canSelectMoreTimesOfDay = canSelectMoreTimesOfDay(
                            selectionCount,
                            numberOfDosage.toIntOrNull() ?: 0
                        ),
                        onStateChange = { count, selected ->
                            isNightSelected = selected
                            selectionCount = count
                        },
                        onShowMaxSelectionError = {
                            showMaxSelectionSnackBar(scope, numberOfDosage, context)
                        }
                    )
                },
                label = { Text(text = TimesOfDay.Night.name) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Selected"
                    )
                }
            )
        }

        Spacer(modifier = Modifier.padding(8.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                validateMedication(
                    name = medicationName,
                    dosage = numberOfDosage.toIntOrNull() ?: 0,
                    recurrence = recurrence,
                    endDate = endDate,
                    morningSelection = isMorningSelected,
                    afternoonSelection = isAfternoonSelected,
                    eveningSelection = isEveningSelected,
                    nightSelection = isNightSelected,
                    onInvalidate = {
                        val invalidatedValue = context.getString(it)
                        Toast.makeText(
                            context,
                            context.getString(R.string.value_is_empty, invalidatedValue),
                            Toast.LENGTH_LONG
                        ).show()

                        val event = String.format(
                            AnalyticsEvents.ADD_MEDICATION_MEDICATION_VALUE_INVALIDATED,
                            invalidatedValue
                        )
                        analyticsHelper.logEvent(event)
                    },
                    onValidate = {
                        navigateToMedicationConfirm(it)
                        analyticsHelper.logEvent(AnalyticsEvents.ADD_MEDICATION_NAVIGATING_TO_MEDICATION_CONFIRM)
                    },
                    viewModel = viewModel
                )
            },
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Text(
                text = stringResource(id = R.string.next),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private fun validateMedication(
    name: String,
    dosage: Int,
    recurrence: String,
    endDate: Long,
    morningSelection: Boolean,
    afternoonSelection: Boolean,
    eveningSelection: Boolean,
    nightSelection: Boolean,
    onInvalidate: (Int) -> Unit,
    onValidate: (List<Medication>) -> Unit,
    viewModel: AddMedicationViewModel
) {
    if (name.isEmpty()) {
        onInvalidate(R.string.medication_name)
        return
    }

    if (dosage < 1) {
        onInvalidate(R.string.dose_per_day)
        return
    }

    if (endDate < 1) {
        onInvalidate(R.string.end_date)
        return
    }

    if (!morningSelection && !afternoonSelection && !eveningSelection && !nightSelection) {
        onInvalidate(R.string.times_of_day)
        return
    }

    val timesOfDay = mutableListOf<TimesOfDay>()
    if (morningSelection) timesOfDay.add(TimesOfDay.Morning)
    if (afternoonSelection) timesOfDay.add(TimesOfDay.Afternoon)
    if (eveningSelection) timesOfDay.add(TimesOfDay.Evening)
    if (nightSelection) timesOfDay.add(TimesOfDay.Night)

    val medications =
        viewModel.createMedications(name, dosage, recurrence, Date(endDate), timesOfDay)

    onValidate(medications)
}

private fun handleSelection(
    isSelected: Boolean,
    selectionCount: Int,
    canSelectMoreTimesOfDay: Boolean,
    onStateChange: (Int, Boolean) -> Unit,
    onShowMaxSelectionError: () -> Unit
) {
    if (isSelected) {
        onStateChange(selectionCount - 1, !isSelected)
    } else {
        if (canSelectMoreTimesOfDay) {
            onStateChange(selectionCount + 1, !isSelected)
        } else {
            onShowMaxSelectionError()
        }
    }
}

private fun canSelectMoreTimesOfDay(selectionCount: Int, numberOfDosage: Int): Boolean {
    return selectionCount < numberOfDosage
}

private fun showMaxSelectionSnackBar(
    scope: CoroutineScope,
    numberOfDosage: String,
    context: Context
) {
    scope.launch {
        // TODO: Fix showing Snackbar.
        // SnackbarHostState().showSnackbar("You can only select ${numberOfDosage} times of days.")
    }
    Toast.makeText(
        context,
        "You're selecting ${(numberOfDosage.toIntOrNull() ?: 0) + 1} time(s) of days which is more than the number of dosage.",
        Toast.LENGTH_LONG
    ).show()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurrenceDropdownMenu(recurrence: (String) -> Unit) {
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
                modifier = Modifier.menuAnchor(),
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
                            recurrence(selectionOption)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndDateTextField(endDate: (Long) -> Unit) {
    Text(
        text = stringResource(id = R.string.end_date),
        style = MaterialTheme.typography.bodyLarge
    )

    var shouldDisplay by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()
    if (isPressed) {
        shouldDisplay = true
    }

    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())

    var selectedDate by rememberSaveable {
        mutableStateOf(
            datePickerState.selectedDateMillis!!.dateInMillisToFormattedString()
        )
    }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        value = selectedDate,
        onValueChange = {},
        trailingIcon = { Icons.Default.DateRange },
        interactionSource = interactionSource
    )

    DatePickerDialog(
        state = datePickerState,
        shouldDisplay = shouldDisplay,
        onConfirmClicked = { selectedDateInMillis ->
            selectedDate = selectedDateInMillis.dateInMillisToFormattedString()
            endDate(selectedDateInMillis)
        },
        dismissRequest = {
            shouldDisplay = false
        }
    )
}
