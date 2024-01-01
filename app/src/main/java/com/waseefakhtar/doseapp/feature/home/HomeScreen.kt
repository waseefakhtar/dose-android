package com.waseefakhtar.doseapp.feature.home

import android.Manifest
import android.app.AlarmManager
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.waseefakhtar.doseapp.R
import com.waseefakhtar.doseapp.analytics.AnalyticsEvents
import com.waseefakhtar.doseapp.analytics.AnalyticsHelper
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.extension.toFormattedDateShortString
import com.waseefakhtar.doseapp.extension.toFormattedDateString
import com.waseefakhtar.doseapp.extension.toFormattedMonthDateString
import com.waseefakhtar.doseapp.feature.addmedication.navigation.AddMedicationDestination
import com.waseefakhtar.doseapp.feature.home.data.CalendarDataSource
import com.waseefakhtar.doseapp.feature.home.model.CalendarModel
import com.waseefakhtar.doseapp.feature.home.viewmodel.HomeState
import com.waseefakhtar.doseapp.feature.home.viewmodel.HomeViewModel
import java.time.LocalDate

@Composable
fun HomeRoute(
    navController: NavController,
    askNotificationPermission: Boolean,
    askAlarmPermission: Boolean,
    navigateToMedicationDetail: (Medication) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val analyticsHelper = AnalyticsHelper.getInstance(LocalContext.current)
    val state = viewModel.state
    PermissionAlarmDialog(analyticsHelper, askAlarmPermission)
    PermissionDialog(analyticsHelper, askNotificationPermission)
    HomeScreen(navController, analyticsHelper, state, viewModel, navigateToMedicationDetail)
}

@Composable
fun HomeScreen(
    navController: NavController,
    analyticsHelper: AnalyticsHelper,
    state: HomeState,
    viewModel: HomeViewModel,
    navigateToMedicationDetail: (Medication) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DailyMedications(
            navController = navController,
            analyticsHelper = analyticsHelper,
            state = state,
            viewModel = viewModel,
            navigateToMedicationDetail = navigateToMedicationDetail
        )
    }
}

@Composable
fun Greeting() {
    Column {
        // TODO: Add greeting based on time of day e.g. Good Morning, Good Afternoon, Good evening.
        // TODO: Get name from DB and show user's first name.
        Text(
            text = "Good morning,",
            style = MaterialTheme.typography.displaySmall
        )
        Text(
            text = "Kathryn!",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.padding(8.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyOverviewCard(
    navController: NavController,
    analyticsHelper: AnalyticsHelper,
    medicationsToday: List<Medication>
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .height(156.dp),
        shape = RoundedCornerShape(36.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.tertiary
        ),
        onClick = {
            analyticsHelper.logEvent(AnalyticsEvents.ADD_MEDICATION_CLICKED_DAILY_OVERVIEW)
            navController.navigate(AddMedicationDestination.route)
        }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = stringResource(R.string.your_plan_for_today),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = stringResource(
                        id = R.string.daily_medicine_log,
                        medicationsToday.filter { it.medicationTaken }.size,
                        medicationsToday.size
                    ),
                    style = MaterialTheme.typography.titleSmall,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    painter = painterResource(id = R.drawable.doctor), contentDescription = ""
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmptyCard(navController: NavController, analyticsHelper: AnalyticsHelper) {
    analyticsHelper.logEvent(AnalyticsEvents.EMPTY_CARD_SHOWN)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(156.dp),
        shape = RoundedCornerShape(36.dp),
        colors = cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.tertiary
        ),
        onClick = {
            analyticsHelper.logEvent(AnalyticsEvents.ADD_MEDICATION_CLICKED_EMPTY_CARD)
            navController.navigate(AddMedicationDestination.route)
        }
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(24.dp, 24.dp, 0.dp, 16.dp)
                    .fillMaxWidth(.50F)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {

                Text(
                    text = stringResource(R.string.medication_break),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = stringResource(R.string.home_screen_empty_card_message),
                    style = MaterialTheme.typography.titleSmall,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Image(
                    painter = painterResource(id = R.drawable.doctor), contentDescription = ""
                )
            }
        }
    }
}

@Composable
fun DailyMedications(
    navController: NavController,
    analyticsHelper: AnalyticsHelper,
    state: HomeState,
    viewModel: HomeViewModel,
    navigateToMedicationDetail: (Medication) -> Unit
) {

    var filteredMedications: List<Medication> by remember { mutableStateOf(emptyList()) }

    DatesHeader(analyticsHelper) { selectedDate ->
        val newMedicationList = state.medications
            .filter { medication ->
                medication.medicationTime.toFormattedDateString() == selectedDate.date.toFormattedDateString()
            }
            .sortedBy { it.medicationTime }

        filteredMedications = newMedicationList
        analyticsHelper.logEvent(AnalyticsEvents.HOME_NEW_DATE_SELECTED)
    }

    if (filteredMedications.isEmpty()) {
        EmptyCard(navController, analyticsHelper)
    } else {
        LazyColumn(
            modifier = Modifier,
        ) {
            items(
                items = filteredMedications,
                itemContent = {
                    MedicationCard(
                        medication = it,
                        navigateToMedicationDetail = { medication ->
                            navigateToMedicationDetail(medication)
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun DatesHeader(
    analyticsHelper: AnalyticsHelper,
    onDateSelected: (CalendarModel.DateModel) -> Unit // Callback to pass the selected date
) {
    val dataSource = CalendarDataSource()
    var calendarModel by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        DateHeader(
            data = calendarModel,
            onPrevClickListener = { startDate ->
                // refresh the CalendarModel with new data
                // by get data with new Start Date (which is the startDate-1 from the visibleDates)
//                val calendar = Calendar.getInstance()
//                calendar.time = startDate
//
//                calendar.add(Calendar.DAY_OF_YEAR, -2) // Subtract one day from startDate
//                val finalStartDate = calendar.time

                calendarModel = dataSource.getData(
                    startDate = startDate.minusDays(2),
                    lastSelectedDate = calendarModel.selectedDate.date
                )
                analyticsHelper.logEvent(AnalyticsEvents.HOME_CALENDAR_PREVIOUS_WEEK_CLICKED)
            },
            onNextClickListener = { endDate ->
                // refresh the CalendarModel with new data
                // by get data with new Start Date (which is the endDate+2 from the visibleDates)
//                val calendar = Calendar.getInstance()
//                calendar.time = endDate
//
//                calendar.add(Calendar.DAY_OF_YEAR, 2)
//                val finalStartDate = calendar.time

                calendarModel = dataSource.getData(
                    startDate = endDate.plusDays(2),
                    lastSelectedDate = calendarModel.selectedDate.date
                )
                analyticsHelper.logEvent(AnalyticsEvents.HOME_CALENDAR_NEXT_WEEK_CLICKED)
            }
        )
        DateList(
            data = calendarModel,
            onDateClickListener = { date ->
                calendarModel = calendarModel.copy(
                    selectedDate = date,
                    visibleDates = calendarModel.visibleDates.map {
                        it.copy(
                            isSelected = it.date.toFormattedDateString() == date.date.toFormattedDateString()
                        )
                    }
                )
                onDateSelected(date)
            }
        )
    }
}

@Composable
fun DateList(
    data: CalendarModel,
    onDateClickListener: (CalendarModel.DateModel) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(items = data.visibleDates) { date ->
            DateItem(date, onDateClickListener)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateItem(
    date: CalendarModel.DateModel,
    onClickListener: (CalendarModel.DateModel) -> Unit,
) {
    Column {
        Text(
            text = date.day, // day "Mon", "Tue"
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.outline
        )
        Card(
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 4.dp),
            onClick = { onClickListener(date) },
            colors = cardColors(
                // background colors of the selected date
                // and the non-selected date are different
                containerColor = if (date.isSelected) {
                    MaterialTheme.colorScheme.tertiary
                } else {
                    MaterialTheme.colorScheme.surface
                }
            ),
        ) {
            Column(
                modifier = Modifier
                    .width(42.dp)
                    .height(42.dp)
                    .padding(8.dp)
                    .fillMaxSize(), // Fill the available size in the Column
                verticalArrangement = Arrangement.Center, // Center vertically
                horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
            ) {
                Text(
                    text = date.date.toFormattedDateShortString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (date.isSelected) {
                        FontWeight.Medium
                    } else {
                        FontWeight.Normal
                    }
                )
            }
        }
    }
}

@Composable
fun DateHeader(
    data: CalendarModel,
    onPrevClickListener: (LocalDate) -> Unit,
    onNextClickListener: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            text = if (data.selectedDate.isToday) {
                stringResource(R.string.today)
            } else {
                data.selectedDate.date.toFormattedMonthDateString()
            },
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary
        )
        IconButton(
            onClick = {
                onPrevClickListener(data.startDate.date)
            },
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                tint = MaterialTheme.colorScheme.tertiary,
                contentDescription = "Back"
            )
        }
        IconButton(
            onClick = {
                onNextClickListener(data.endDate.date)
            },
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                tint = MaterialTheme.colorScheme.tertiary,
                contentDescription = "Next"
            )
        }
    }
}

sealed class MedicationListItem {
    data class OverviewItem(
        val medicationsToday: List<Medication>,
        val isMedicationListEmpty: Boolean
    ) : MedicationListItem()

    data class MedicationItem(val medication: Medication) : MedicationListItem()
    data class HeaderItem(val headerText: String) : MedicationListItem()
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionDialog(
    analyticsHelper: AnalyticsHelper,
    askNotificationPermission: Boolean,
) {
    if (askNotificationPermission && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)) {
        val notificationPermissionState =
            rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS) { isGranted ->
                when (isGranted) {
                    true -> analyticsHelper.logEvent(AnalyticsEvents.NOTIFICATION_PERMISSION_GRANTED)
                    false -> analyticsHelper.logEvent(AnalyticsEvents.NOTIFICATION_PERMISSION_REFUSED)
                }
            }
        if (!notificationPermissionState.status.isGranted) {
            val openAlertDialog = remember { mutableStateOf(true) }

            when {
                openAlertDialog.value -> {
                    analyticsHelper.logEvent(AnalyticsEvents.NOTIFICATION_PERMISSION_DIALOG_SHOWN)
                    AlertDialog(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = stringResource(R.string.notifications)
                            )
                        },
                        title = {
                            Text(text = stringResource(R.string.notification_permission_required))
                        },
                        text = {
                            Text(text = stringResource(R.string.notification_permission_required_description_message))
                        },
                        onDismissRequest = {
                            openAlertDialog.value = false
                            analyticsHelper.logEvent(AnalyticsEvents.NOTIFICATION_PERMISSION_DIALOG_DISMISSED)
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    notificationPermissionState.launchPermissionRequest()
                                    openAlertDialog.value = false
                                    analyticsHelper.logEvent(AnalyticsEvents.NOTIFICATION_PERMISSION_DIALOG_ALLOW_CLICKED)
                                }
                            ) {
                                Text(stringResource(R.string.allow))
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionAlarmDialog(analyticsHelper: AnalyticsHelper, askAlarmPermission: Boolean) {
    val context = LocalContext.current
    val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
    if (askAlarmPermission && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)) {
        val alarmPermissionState =
            rememberPermissionState(Manifest.permission.SCHEDULE_EXACT_ALARM) { isGranted ->
                when (isGranted) {
                    true -> analyticsHelper.logEvent(AnalyticsEvents.ALARM_PERMISSION_GRANTED)
                    false -> analyticsHelper.logEvent(AnalyticsEvents.ALARM_PERMISSION_REFUSED)
                }
            }
        if (alarmManager?.canScheduleExactAlarms() == false) {
            val openAlertDialog = remember { mutableStateOf(true) }

            when {
                openAlertDialog.value -> {
                    analyticsHelper.logEvent(AnalyticsEvents.ALARM_PERMISSION_DIALOG_SHOWN)

                    AlertDialog(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = stringResource(R.string.alarms)
                            )
                        },
                        title = {
                            Text(text = stringResource(R.string.alarms_permission_required))
                        },
                        text = {
                            Text(text = stringResource(R.string.alarms_permission_required_description_message))
                        },
                        onDismissRequest = {
                            openAlertDialog.value = false
                            analyticsHelper.logEvent(AnalyticsEvents.ALARM_PERMISSION_DIALOG_DISMISSED)
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    Intent().also { intent ->
                                        intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                                        context.startActivity(intent)
                                    }

                                    openAlertDialog.value = false
                                    analyticsHelper.logEvent(AnalyticsEvents.ALARM_PERMISSION_DIALOG_ALLOW_CLICKED)
                                }
                            ) {
                                Text(stringResource(R.string.allow))
                            }
                        }
                    )
                }
            }
        }
    }
}
