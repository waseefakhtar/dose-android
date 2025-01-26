package com.waseefakhtar.doseapp.feature.addmedication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waseefakhtar.doseapp.R
import com.waseefakhtar.doseapp.analytics.AnalyticsEvents
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.extension.toFormattedMonthDateString
import com.waseefakhtar.doseapp.feature.addmedication.model.CalendarInformation
import com.waseefakhtar.doseapp.feature.addmedication.viewmodel.AddMedicationViewModel
import com.waseefakhtar.doseapp.util.Frequency
import com.waseefakhtar.doseapp.util.HOUR_MINUTE_FORMAT
import com.waseefakhtar.doseapp.util.MedicationType
import com.waseefakhtar.doseapp.util.SnackbarUtil.Companion.showSnackbar
import com.waseefakhtar.doseapp.util.getFrequencyList
import java.util.Calendar
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
    viewModel: AddMedicationViewModel = hiltViewModel(),
) {
    AddMedicationScreen(onBackClicked, viewModel, navigateToMedicationConfirm)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreen(
    onBackClicked: () -> Unit,
    viewModel: AddMedicationViewModel,
    navigateToMedicationConfirm: (List<Medication>) -> Unit,
) {
    var medicationName by rememberSaveable { mutableStateOf("") }
    var numberOfDosage by rememberSaveable { mutableStateOf("") }
    var frequency by rememberSaveable { mutableStateOf(Frequency.EVERYDAY.name) }
    var startDate by rememberSaveable { mutableLongStateOf(0L) }
    var endDate by rememberSaveable { mutableLongStateOf(0L) }
    var showDatePicker by remember { mutableStateOf(false) }
    val selectedTimes =
        rememberSaveable(
            saver = CalendarInformation.getStateListSaver(),
        ) { mutableStateListOf(CalendarInformation(Calendar.getInstance())) }
    val context = LocalContext.current
    var medicationType by rememberSaveable { mutableStateOf(MedicationType.getDefault()) }

    fun addTime(time: CalendarInformation) {
        selectedTimes.add(time)
        viewModel.logEvent(eventName = AnalyticsEvents.ADD_MEDICATION_ADD_TIME_CLICKED)
    }

    fun removeTime(time: CalendarInformation) {
        selectedTimes.remove(time)
        viewModel.logEvent(eventName = AnalyticsEvents.ADD_MEDICATION_DELETE_TIME_CLICKED)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier =
                Modifier
                    .padding(vertical = 16.dp),
                navigationIcon = {
                    FloatingActionButton(
                        onClick = {
                            viewModel.logEvent(eventName = AnalyticsEvents.ADD_MEDICATION_ON_BACK_CLICKED)
                            onBackClicked()
                        },
                        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
                title = {
                    Text(
                        modifier = Modifier.padding(16.dp, 0.dp),
                        text = stringResource(id = R.string.add_medication),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.displaySmall,
                    )
                },
            )
        },
        bottomBar = {
            Button(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(56.dp),
                onClick = {
                    validateMedication(
                        name = medicationName,
                        dosage = numberOfDosage.toIntOrNull() ?: 1,
                        frequency = frequency,
                        startDate = startDate,
                        endDate = endDate,
                        selectedTimes = selectedTimes,
                        type = medicationType,
                        onInvalidate = {
                            val invalidatedValue = context.getString(it)
                            showSnackbar(
                                context.getString(
                                    R.string.value_is_empty,
                                    invalidatedValue,
                                ),
                            )

                            val event =
                                String.format(
                                    AnalyticsEvents.ADD_MED_VALUE_INVALIDATED,
                                    invalidatedValue.lowercase(),
                                )
                            viewModel.logEvent(eventName = event)
                        },
                        onValidate = {
                            navigateToMedicationConfirm(it)
                            viewModel.logEvent(eventName = AnalyticsEvents.ADD_MED_NAVIGATING_TO_MED_CONFIRM)
                        },
                        viewModel = viewModel,
                    )
                },
                shape = MaterialTheme.shapes.extraLarge,
            ) {
                Text(
                    text = stringResource(id = R.string.next),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier =
            Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = medicationName,
                onValueChange = { medicationName = it },
                label = { Text(stringResource(id = R.string.medication_name)) },
                placeholder = { Text(stringResource(R.string.medication_name_hint)) },
                singleLine = true,
            )

            Spacer(modifier = Modifier.padding(4.dp))

            FrequencyDropdownMenu { frequency = it }

            Spacer(modifier = Modifier.padding(4.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    value = buildDateRangeText(startDate, endDate),
                    onValueChange = {},
                    label = { Text(stringResource(R.string.duration)) },
                    placeholder = { Text(stringResource(R.string.select_duration)) },
                    trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect {
                                if (it is PressInteraction.Release) {
                                    showDatePicker = true
                                }
                            }
                        }
                    },
                )
            }

            DateRangePickerDialog(
                showDialog = showDatePicker,
                startDate = startDate,
                endDate = endDate,
                onDismiss = { showDatePicker = false },
                onDateSelected = { start, end ->
                    startDate = start
                    endDate = end
                },
            )

            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = stringResource(R.string.schedule),
                style = MaterialTheme.typography.bodyLarge,
            )

            for (index in selectedTimes.indices) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TimerTextField(
                        modifier = Modifier.weight(1f),
                        isLastItem = selectedTimes.lastIndex == index,
                        isOnlyItem = selectedTimes.size == 1,
                        time = {
                            selectedTimes[index] = it
                        },
                        onDeleteClick = { removeTime(selectedTimes[index]) },
                        logEvent = {
                            viewModel.logEvent(AnalyticsEvents.ADD_MEDICATION_NEW_TIME_SELECTED)
                        },
                    )

                    if (index == selectedTimes.lastIndex) {
                        Button(
                            onClick = { addTime(CalendarInformation(Calendar.getInstance())) },
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.add_time),
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.padding(4.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = numberOfDosage,
                onValueChange = {
                    if (it.isEmpty() || it.toIntOrNull() != null) {
                        if (it.toIntOrNull() ?: 0 <= 3) {
                            numberOfDosage = it
                        }
                    }
                },
                label = { Text(stringResource(id = R.string.dose_optional)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text(stringResource(R.string.dosage_hint)) },
                singleLine = true,
            )

            Spacer(modifier = Modifier.padding(4.dp))

            Column {
                Text(
                    text = stringResource(R.string.type),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 0.dp, bottom = 8.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(horizontal = 0.dp),
                    userScrollEnabled = false
                ) {
                    items(MedicationType.entries) { type ->
                        MedicationTypeBox(
                            type = type,
                            isSelected = type == medicationType,
                            onSelect = { medicationType = type }
                        )
                    }
                }
            }
        }
    }
}

private fun validateMedication(
    name: String,
    dosage: Int,
    frequency: String,
    startDate: Long,
    endDate: Long,
    selectedTimes: List<CalendarInformation>,
    type: MedicationType,
    onInvalidate: (Int) -> Unit,
    onValidate: (List<Medication>) -> Unit,
    viewModel: AddMedicationViewModel,
) {
    if (name.isEmpty()) {
        onInvalidate(R.string.medication_name)
        return
    }

    if (dosage < 1) {
        onInvalidate(R.string.dose_per_day)
        return
    }

    if (startDate == 0L || endDate == 0L) {
        onInvalidate(R.string.duration)
        return
    }

    if (startDate >= endDate) {
        onInvalidate(R.string.duration)
        return
    }

    if (selectedTimes.isEmpty()) {
        onInvalidate(R.string.schedule)
        return
    }

    val medications =
        viewModel.createMedications(
            name = name,
            dosage = dosage,
            frequency = frequency,
            startDate = Date(startDate),
            endDate = Date(endDate),
            medicationTimes = selectedTimes,
            type = type
        )

    onValidate(medications)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrequencyDropdownMenu(frequency: (String) -> Unit) {
    val options = getFrequencyList()
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            modifier =
            Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            value =
            when (selectedOption.stringResId) {
                R.string.every_n_days -> stringResource(
                    selectedOption.stringResId,
                    selectedOption.days
                )

                R.string.every_n_weeks -> stringResource(
                    selectedOption.stringResId,
                    selectedOption.days / 7
                )

                else -> stringResource(selectedOption.stringResId)
            },
            onValueChange = {},
            label = { Text(stringResource(id = R.string.frequency)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            when (option.stringResId) {
                                R.string.every_n_days -> stringResource(option.stringResId, option.days)
                                R.string.every_n_weeks -> stringResource(option.stringResId, option.days / 7)
                                else -> stringResource(option.stringResId)
                            },
                        )
                    },
                    onClick = {
                        selectedOption = option
                        frequency(option.name)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
fun TimerTextField(
    modifier: Modifier = Modifier,
    isLastItem: Boolean,
    isOnlyItem: Boolean,
    time: (CalendarInformation) -> Unit,
    onDeleteClick: () -> Unit,
    logEvent: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()
    val currentTime = CalendarInformation(Calendar.getInstance())
    var selectedTime by rememberSaveable(
        stateSaver = CalendarInformation.getStateSaver(),
    ) { mutableStateOf(currentTime) }

    TimePickerDialogComponent(
        showDialog = isPressed,
        selectedDate = selectedTime,
        onSelectedTime = {
            logEvent.invoke()
            selectedTime = it
            time(it)
        },
    )

    TextField(
        modifier = modifier.fillMaxWidth(),
        readOnly = true,
        value = selectedTime.getDateFormatted(HOUR_MINUTE_FORMAT),
        onValueChange = {},
        trailingIcon = {
            if (isLastItem && !isOnlyItem) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
            }
        },
        interactionSource = interactionSource,
    )
}

@Composable
private fun buildDateRangeText(
    startDate: Long,
    endDate: Long,
): String =
    if (startDate == 0L || endDate == 0L) {
        ""
    } else {
        "${Date(startDate).toFormattedMonthDateString()} - ${Date(endDate).toFormattedMonthDateString()}"
    }

@Composable
private fun MedicationTypeBox(
    type: MedicationType,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface
            )
            .border(
                width = 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onSelect)
            .padding(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(
                    when (type) {
                        MedicationType.TABLET -> R.drawable.ic_tablet
                        MedicationType.CAPSULE -> R.drawable.ic_capsule
                        MedicationType.SYRUP -> R.drawable.ic_syrup
                        MedicationType.DROPS -> R.drawable.ic_drops
                        MedicationType.SPRAY -> R.drawable.ic_spray
                        MedicationType.GEL -> R.drawable.ic_gel
                    }
                ),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(
                    when (type) {
                        MedicationType.TABLET -> R.string.tablet
                        MedicationType.CAPSULE -> R.string.capsule
                        MedicationType.SYRUP -> R.string.type_syrup
                        MedicationType.DROPS -> R.string.drops
                        MedicationType.SPRAY -> R.string.spray
                        MedicationType.GEL -> R.string.gel
                    }
                ),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
