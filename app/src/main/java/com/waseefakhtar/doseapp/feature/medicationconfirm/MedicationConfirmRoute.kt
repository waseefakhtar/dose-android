package com.waseefakhtar.doseapp.feature.medicationconfirm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.waseefakhtar.doseapp.R
import com.waseefakhtar.doseapp.analytics.AnalyticsEvents
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.extension.toFormattedDateString
import com.waseefakhtar.doseapp.feature.medicationconfirm.viewmodel.MedicationConfirmState
import com.waseefakhtar.doseapp.feature.medicationconfirm.viewmodel.MedicationConfirmViewModel
import com.waseefakhtar.doseapp.util.SnackbarUtil.Companion.showSnackbar

@Composable
fun MedicationConfirmRoute(
    medication: List<Medication>?,
    onBackClicked: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MedicationConfirmViewModel = hiltViewModel()
) {
    medication?.let {
        MedicationConfirmScreen(
            medications = it,
            viewModel = viewModel,
            onBackClicked = onBackClicked,
            navigateToHome = navigateToHome,
            logEvent = viewModel::logEvent
        )
    } ?: {
        FirebaseCrashlytics.getInstance().log("Error: Cannot show MedicationConfirmScreen. Medication is null.")
    }
}

@Composable
fun MedicationConfirmScreen(
    medications: List<Medication>,
    viewModel: MedicationConfirmViewModel,
    logEvent: (String) -> Unit,
    onBackClicked: () -> Unit,
    navigateToHome: () -> Unit,
) {

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel
            .isMedicationSaved
            .collect {
                showSnackbar(
                    context.getString(
                        R.string.medication_timely_reminders_setup_message,
                        medications.first().name
                    )
                )
                navigateToHome()
                logEvent.invoke(AnalyticsEvents.MEDICATIONS_SAVED)
            }
    }

    Column(
        modifier = Modifier.padding(0.dp, 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FloatingActionButton(
            onClick = {
                logEvent.invoke(AnalyticsEvents.MEDICATION_CONFIRM_ON_BACK_CLICKED)
                onBackClicked()
            },
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.back)
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.all_done),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall
        )

        val medication = medications.first()
        Text(
            text = pluralStringResource(
                id = R.plurals.all_set,
                count = medications.size,
                medication.name,
                medications.size,
                medication.frequency.lowercase(),
                medication.endDate.toFormattedDateString()
            ),
            style = MaterialTheme.typography.titleMedium
        )
    }

    Column(
        modifier = Modifier
            .padding(0.dp, 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                logEvent.invoke(AnalyticsEvents.MEDICATION_CONFIRM_ON_CONFIRM_CLICKED)
                viewModel.addMedication(MedicationConfirmState(medications))
            },
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Text(
                text = stringResource(id = R.string.confirm),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
