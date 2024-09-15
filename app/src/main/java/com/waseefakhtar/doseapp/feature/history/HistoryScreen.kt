package com.waseefakhtar.doseapp.feature.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waseefakhtar.doseapp.R
import com.waseefakhtar.doseapp.analytics.AnalyticsEvents
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.extension.hasPassed
import com.waseefakhtar.doseapp.extension.toFormattedDateShortString
import com.waseefakhtar.doseapp.extension.toFormattedMonthString
import com.waseefakhtar.doseapp.feature.history.viewmodel.HistoryState
import com.waseefakhtar.doseapp.feature.history.viewmodel.HistoryViewModel
import com.waseefakhtar.doseapp.feature.home.MedicationCard
import com.waseefakhtar.doseapp.feature.home.MedicationListItem
import com.waseefakhtar.doseapp.feature.home.data.CalendarMonthDataSource
import com.waseefakhtar.doseapp.feature.home.model.CalendarMonthModel
import java.util.Calendar
import java.util.Date

@Composable
fun HistoryRoute(
    navigateToMedicationDetail: (Medication) -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state = viewModel.state
    HistoryScreen(
        state = state,
        navigateToMedicationDetail = navigateToMedicationDetail,
        logEvent = viewModel::logEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    state: HistoryState,
    navigateToMedicationDetail: (Medication) -> Unit,
    logEvent: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(top = 16.dp),
                title = {
                    Text(
                        text = stringResource(id = R.string.history),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.displaySmall,
                    )
                }
            )
        },
        bottomBar = { },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MedicationList(
                state = state,
                navigateToMedicationDetail = navigateToMedicationDetail,
                logEvent = logEvent
            )
        }
    }
}

@Composable
fun MedicationList(
    state: HistoryState,
    navigateToMedicationDetail: (Medication) -> Unit,
    logEvent: (String) -> Unit
) {

    val filteredMedicationList = state.medications.filter { it.medicationTime.hasPassed() }
    val sortedMedicationList: List<MedicationListItem> =
        filteredMedicationList.sortedBy { it.medicationTime }
            .map { MedicationListItem.MedicationItem(it) }

    when (sortedMedicationList.isEmpty()) {
        true -> EmptyView(state, logEvent)
        false -> MedicationLazyColumn(
            sortedMedicationList,
            navigateToMedicationDetail,
            state,
            logEvent
        )
    }
}

@Composable
fun MedicationLazyColumn(
    sortedMedicationList: List<MedicationListItem>,
    navigateToMedicationDetail: (Medication) -> Unit,
    state: HistoryState,
    logEvent: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        HistoryDatesHeader(
            lastSelectedDate = state.lastSelectedDate,
            logEvent = { logEvent.invoke(it) },
            state = state
        )

        sortedMedicationList.forEach {
            when (it) {
                is MedicationListItem.OverviewItem -> {}
                is MedicationListItem.HeaderItem -> {
                    Text(
                        modifier = Modifier
                            .padding(4.dp, 12.dp, 8.dp, 0.dp)
                            .fillMaxWidth(),
                        text = it.headerText.uppercase(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

                is MedicationListItem.MedicationItem -> {
                    MedicationCard(
                        medication = it.medication,
                        navigateToMedicationDetail = { medication ->
                            navigateToMedicationDetail(medication)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyView(
    state: HistoryState,
    logEvent: (String) -> Unit
) {

    HistoryDatesHeader(
        lastSelectedDate = state.lastSelectedDate,
        logEvent = {
            logEvent.invoke(it)
        },
        state = state
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineMedium,
            text = stringResource(id = R.string.no_history_yet),
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
fun HistoryDatesHeader(
    lastSelectedDate: String,
    logEvent: (String) -> Unit,
    state: HistoryState
) {
    val dataSource = CalendarMonthDataSource()
    var calendarModel by remember {
        mutableStateOf(
            dataSource.getMonthData(
                lastSelectedDate = dataSource.getLastSelectedDate(lastSelectedDate),
                dataList = state.medications
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        HistoryDateHeader(
            data = calendarModel,
            dataSource = dataSource,
            onPrevClickListener = { startDate ->
                val calendar = Calendar.getInstance()
                calendar.time = startDate

                calendar.add(Calendar.DAY_OF_YEAR, -2)
                val finalStartDate = calendar.time

                calendarModel = dataSource.getMonthData(
                    startDate = finalStartDate,
                    lastSelectedDate = calendarModel.selectedDate.date,
                    dataList = state.medications
                )
                logEvent.invoke(AnalyticsEvents.HISTORY_CALENDAR_PREVIOUS_WEEK_CLICKED)
            },
            onNextClickListener = { endDate ->
                val calendar = Calendar.getInstance()
                calendar.time = endDate

                calendar.add(Calendar.DAY_OF_YEAR, 2)
                val finalStartDate = calendar.time

                calendarModel = dataSource.getMonthData(
                    startDate = finalStartDate,
                    lastSelectedDate = calendarModel.selectedDate.date,
                    dataList = state.medications
                )
                logEvent.invoke(AnalyticsEvents.HISTORY_CALENDAR_NEXT_WEEK_CLICKED)
            }
        )
        HistoryDateList(data = calendarModel)
    }
}

@Composable
fun HistoryDateHeader(
    data: CalendarMonthModel,
    dataSource: CalendarMonthDataSource,
    onPrevClickListener: (Date) -> Unit,
    onNextClickListener: (Date) -> Unit
) {
    Row {
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            text = data.startDate.date.toFormattedMonthString(),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.headlineSmall
        )
        IconButton(onClick = {
            onPrevClickListener(data.startDate.date)
        }) {
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
            enabled = !dataSource.isCurrentMonth(data.startDate.date)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                tint = MaterialTheme.colorScheme.tertiary,
                contentDescription = "Next"
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HistoryDateList(data: CalendarMonthModel) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),

        ) {
        data.visibleDates.forEach { date ->
            HistoryDateItem(date)
        }
    }
}


@Composable
fun HistoryDateItem(
    date: CalendarMonthModel.MonthModel,
) {
    Card(
        modifier = Modifier.padding(4.dp),
        colors = cardColors(
            containerColor = when (date.status) {
                1 -> Color.Green.copy(0.5f)
                2 -> Color.Yellow.copy(0.5f)
                3 -> Color.Red.copy(0.5f)
                else -> MaterialTheme.colorScheme.surface
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
                fontWeight = FontWeight.Normal
            )
        }
    }
}
