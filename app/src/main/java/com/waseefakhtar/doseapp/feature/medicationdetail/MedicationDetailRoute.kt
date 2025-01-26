package com.waseefakhtar.doseapp.feature.medicationdetail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waseefakhtar.doseapp.R
import com.waseefakhtar.doseapp.analytics.AnalyticsEvents
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.extension.toFormattedDateString
import com.waseefakhtar.doseapp.extension.toFormattedTimeString
import com.waseefakhtar.doseapp.feature.medicationdetail.viewmodel.MedicationDetailViewModel
import com.waseefakhtar.doseapp.util.MedicationType
import com.waseefakhtar.doseapp.util.SnackbarUtil.Companion.showSnackbar

@Composable
fun MedicationDetailRoute(
    medicationId: Long?,
    onBackClicked: () -> Unit,
    viewModel: MedicationDetailViewModel = hiltViewModel()
) {
    val medication by viewModel.medication.collectAsState()

    LaunchedEffect(Unit) {
        medicationId?.let {
            viewModel.getMedicationById(it)
        }
    }

    medication?.let {
        MedicationDetailScreen(
            medication = it,
            viewModel = viewModel,
            onBackClicked = onBackClicked
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationDetailScreen(
    medication: Medication,
    viewModel: MedicationDetailViewModel,
    onBackClicked: () -> Unit
) {
    val (cardColor, boxColor, textColor) = medication.type.getCardColor()
    var isTakenTapped by remember(medication.medicationTaken) {
        mutableStateOf(medication.medicationTaken)
    }
    var isSkippedTapped by remember(medication.medicationTaken) {
        mutableStateOf(!medication.medicationTaken)
    }

    val context = LocalContext.current

    LaunchedEffect(medication) {
        isTakenTapped = medication.medicationTaken
        isSkippedTapped = !medication.medicationTaken
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(vertical = 16.dp),
                navigationIcon = {
                    FloatingActionButton(
                        onClick = {
                            viewModel.logEvent(AnalyticsEvents.MEDICATION_DETAIL_ON_BACK_CLICKED)
                            onBackClicked()
                        },
                        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                title = {}
            )
        },
        bottomBar = {
            Column {
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    SegmentedButton(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        selected = isTakenTapped,
                        shape = MaterialTheme.shapes.extraLarge,
                        onClick = {
                            isTakenTapped = !isTakenTapped
                            if (isTakenTapped) {
                                isSkippedTapped = false
                            }
                            viewModel.logEvent(AnalyticsEvents.MEDICATION_DETAIL_TAKEN_CLICKED)
                            viewModel.updateMedication(medication, isTakenTapped)
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.taken),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    SegmentedButton(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        selected = isSkippedTapped,
                        shape = MaterialTheme.shapes.extraLarge,
                        onClick = {
                            isSkippedTapped = !isSkippedTapped
                            if (isSkippedTapped) {
                                isTakenTapped = false
                            }
                            viewModel.logEvent(AnalyticsEvents.MEDICATION_DETAIL_SKIPPED_CLICKED)
                            viewModel.updateMedication(medication, isTakenTapped)
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.skipped),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(56.dp),
                    onClick = {
                        viewModel.logEvent(AnalyticsEvents.MEDICATION_DETAIL_DONE_CLICKED)
                        showSnackbar(context.getString(R.string.medication_logged))
                        onBackClicked()
                    },
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Text(
                        text = stringResource(id = R.string.done),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge,
                text = medication.medicationTime.toFormattedDateString(),
                color = Color(boxColor)
            )

            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .size(120.dp)
                    .border(
                        width = 1.5.dp, color = Color(boxColor), shape = RoundedCornerShape(64.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(
                        when (medication.type) {
                            MedicationType.TABLET -> R.drawable.ic_tablet
                            MedicationType.CAPSULE -> R.drawable.ic_capsule
                            MedicationType.SYRUP -> R.drawable.ic_syrup
                            MedicationType.DROPS -> R.drawable.ic_drops
                            MedicationType.SPRAY -> R.drawable.ic_spray
                            MedicationType.GEL -> R.drawable.ic_gel
                        }
                    ),
                    contentDescription = stringResource(
                        when (medication.type) {
                            MedicationType.TABLET -> R.string.tablet
                            MedicationType.CAPSULE -> R.string.capsule
                            MedicationType.SYRUP -> R.string.type_syrup
                            MedicationType.DROPS -> R.string.drops
                            MedicationType.SPRAY -> R.string.spray
                            MedicationType.GEL -> R.string.gel
                        }
                    ),
                    modifier = Modifier.size(64.dp),
                    tint = Color(boxColor)
                )
            }

            Text(
                text = medication.name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge,
                color = Color(boxColor)
            )

            val doseAndType = "${medication.dosage} ${
            stringResource(
                when (medication.type) {
                    MedicationType.TABLET -> R.string.tablet
                    MedicationType.CAPSULE -> R.string.capsule
                    MedicationType.SYRUP -> R.string.type_syrup
                    MedicationType.DROPS -> R.string.drops
                    MedicationType.SPRAY -> R.string.spray
                    MedicationType.GEL -> R.string.gel
                }
            ).lowercase()
            } at ${medication.medicationTime.toFormattedTimeString()}"

            Text(
                text = doseAndType,
                style = MaterialTheme.typography.headlineSmall,
                color = Color(boxColor)
            )
        }
    }
}
