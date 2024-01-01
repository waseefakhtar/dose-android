package com.waseefakhtar.doseapp.feature.addmedication

import android.content.Context
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.waseefakhtar.doseapp.extension.millisSinceEpoch
import com.waseefakhtar.doseapp.extension.toFormattedDateString
import com.waseefakhtar.doseapp.extension.toFormattedTimeString
import com.waseefakhtar.doseapp.feature.addmedication.viewmodel.AddMedicationViewModel
import com.waseefakhtar.doseapp.util.Recurrence
import com.waseefakhtar.doseapp.util.SnackbarUtil.Companion.showSnackbar
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AddMedicationRoute(
        onBackClicked = {},
        navigateToMedicationConfirm = {},
    )
}

@Composable
fun AddMedicationRoute(
    onBackClicked: () -> Unit,
    navigateToMedicationConfirm: (List<Medication>) -> Unit,
    viewModel: AddMedicationViewModel = hiltViewModel()
) {
    val analyticsHelper = AnalyticsHelper.getInstance(LocalContext.current)
    AddMedicationScreen(
        onBackClicked = onBackClicked,
        viewModel = viewModel,
        analyticsHelper = analyticsHelper,
        navigateToMedicationConfirm = navigateToMedicationConfirm
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreen(
    onBackClicked: () -> Unit,
    viewModel: AddMedicationViewModel,
    analyticsHelper: AnalyticsHelper,
    navigateToMedicationConfirm: (List<Medication>) -> Unit,
    context: Context = LocalContext.current
) {
    var medicationName by rememberSaveable { mutableStateOf("") }
    var numberOfDosage by rememberSaveable { mutableStateOf("1") }

    val (recurrence, onRecurrenceChange) = rememberSaveable(stateSaver = StateSavers.recurrenceSaver) {
        mutableStateOf(Recurrence.Daily)
    }

    val (endDate, onEndDateChange) = rememberSaveable(stateSaver = StateSavers.localDateSaver) {
        mutableStateOf(LocalDate.now())
    }

    val selectedTimes = rememberSaveable(saver = StateSavers.localListTimeSaver) {
        mutableStateListOf()
    }

    fun addTime(time: LocalTime) {
        selectedTimes.add(time)
        analyticsHelper.logEvent(AnalyticsEvents.ADD_MEDICATION_ADD_TIME_CLICKED)
    }

    fun removeTime(time: LocalTime) {
        selectedTimes.remove(time)
        analyticsHelper.logEvent(AnalyticsEvents.ADD_MEDICATION_DELETE_TIME_CLICKED)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(vertical = 16.dp),
                navigationIcon = {
                    FloatingActionButton(
                        onClick = {
                            analyticsHelper.logEvent(AnalyticsEvents.ADD_MEDICATION_ON_BACK_CLICKED)
                            onBackClicked()
                        },
                        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                title = {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(id = R.string.add_medication),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            )
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(56.dp),
                onClick = {
                    validateMedication(
                        name = medicationName,
                        dosage = numberOfDosage.toIntOrNull() ?: 0,
                        recurrence = recurrence,
                        endDate = endDate,
                        selectedTimes = selectedTimes,
                        onInvalidate = {
                            val invalidatedValue = context.getString(it)
                            showSnackbar(
                                context.getString(
                                    R.string.value_is_empty,
                                    invalidatedValue
                                )
                            )

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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = stringResource(id = R.string.medication_name),
                style = MaterialTheme.typography.bodyLarge
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = medicationName,
                onValueChange = { medicationName = it },
                placeholder = {
                    Text(
                        text = stringResource(R.string.medication_name_hint)
                    )
                },
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
                                    contentDescription = stringResource(R.string.error),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.dosage_hint)
                            )
                        },
                        isError = isMaxDoseError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                RecurrenceDropdownMenu(recurrence = onRecurrenceChange)
            }

            if (isMaxDoseError) {
                Text(
                    text = stringResource(R.string.max_dosage_error_message),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(modifier = Modifier.padding(4.dp))
            EndDateTextField(endDate = onEndDateChange)

            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = stringResource(R.string.times_for_medication),
                style = MaterialTheme.typography.bodyLarge
            )

            for (index in selectedTimes.indices) {
                TimerTextField(
                    isLastItem = selectedTimes.lastIndex == index,
                    isOnlyItem = selectedTimes.size == 1,
                    time = {
                        selectedTimes[index] = it
                    },
                    onDeleteClick = { removeTime(selectedTimes[index]) },
                    analyticsHelper = analyticsHelper
                )
            }

            Button(
                onClick = { addTime(LocalTime.now()) }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                Text(stringResource(id = R.string.add_time))
            }
        }
    }
}


private fun validateMedication(
    name: String,
    dosage: Int,
    recurrence: Recurrence,
    endDate: LocalDate,
    selectedTimes: List<LocalTime>,
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

    if (endDate < LocalDate.now()) {
        onInvalidate(R.string.end_date)
        return
    }

    if (selectedTimes.isEmpty()) {
        onInvalidate(R.string.times_for_medication)
        return
    }

    val medications = viewModel.createMedications(
        name = name,
        dosage = dosage,
        recurrence = recurrence,
        endDate = endDate,
        selectedTimes
    )

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

private fun showMaxSelectionSnackbar(
    numberOfDosage: String,
    context: Context
) {
    val dosage = ((numberOfDosage.toIntOrNull() ?: 0) + 1).toString()
    showSnackbar(
        context.getString(
            R.string.dosage_and_frequency_mismatch_error_message,
            dosage
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurrenceDropdownMenu(recurrence: (Recurrence) -> Unit) {

    var isDropDownExpanded by remember { mutableStateOf(false) }
    var selectedRecurrence by remember { mutableStateOf(Recurrence.Daily) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.recurrence),
            style = MaterialTheme.typography.bodyLarge
        )
        ExposedDropdownMenuBox(
            expanded = isDropDownExpanded,
            onExpandedChange = { isDropDownExpanded = !isDropDownExpanded },
        ) {
            TextField(
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = selectedRecurrence.name,
                onValueChange = {},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropDownExpanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
            )
            ExposedDropdownMenu(
                expanded = isDropDownExpanded,
                onDismissRequest = { isDropDownExpanded = false },
            ) {
                Recurrence.entries.forEach { entry ->
                    DropdownMenuItem(
                        text = { Text(entry.name) },
                        onClick = {
                            selectedRecurrence = entry
                            recurrence(entry)
                            isDropDownExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndDateTextField(endDate: (LocalDate) -> Unit) {
    Text(
        text = stringResource(id = R.string.end_date),
        style = MaterialTheme.typography.bodyLarge
    )

    var shouldDisplay by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()
    if (isPressed) shouldDisplay = true


    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= LocalDate.now().millisSinceEpoch
            }
        }
    )

    var selectedDate by rememberSaveable {
        mutableStateOf(
            datePickerState.selectedDateMillis?.toFormattedDateString() ?: ""
        )
    }

    EndDatePickerDialog(
        state = datePickerState,
        shouldDisplay = shouldDisplay,
        onConfirmClicked = { selectedDateInMillis ->
            selectedDate = selectedDateInMillis.toFormattedDateString()
            val date = Instant.ofEpochMilli(selectedDateInMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            endDate(date)
        },
        dismissRequest = { shouldDisplay = false }
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        value = selectedDate,
        onValueChange = {},
        trailingIcon = { Icons.Default.DateRange },
        interactionSource = interactionSource
    )
}

@Composable
fun TimerTextField(
    isLastItem: Boolean,
    isOnlyItem: Boolean,
    time: (LocalTime) -> Unit,
    onDeleteClick: () -> Unit,
    analyticsHelper: AnalyticsHelper,
    currentTime: LocalTime = LocalTime.now()
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()

    var selectedTime by rememberSaveable(stateSaver = StateSavers.localTimeSaver) {
        mutableStateOf(currentTime)
    }

    TimePickerDialogComponent(
        showDialog = isPressed,
        selectedTime = selectedTime,
        onSelectedTime = {
            analyticsHelper.logEvent(AnalyticsEvents.ADD_MEDICATION_NEW_TIME_SELECTED)
            selectedTime = it
            time(it)
        }
    )

    val selectedTimeHourFormat by remember(selectedTime) {
        derivedStateOf(selectedTime::toFormattedTimeString)
    }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        value = selectedTimeHourFormat,
        onValueChange = {},
        trailingIcon = {
            // TODO: Make delete action work properly
            if (isLastItem && !isOnlyItem) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        },
        interactionSource = interactionSource
    )
}
