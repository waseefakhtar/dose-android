package com.waseefakhtar.doseapp.feature.medicationconfirm

import android.widget.Toast
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.waseefakhtar.doseapp.R
import com.waseefakhtar.doseapp.analytics.AnalyticsEvents
import com.waseefakhtar.doseapp.analytics.AnalyticsHelper
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.extension.toFormattedDateString
import com.waseefakhtar.doseapp.feature.medicationconfirm.viewmodel.MedicationConfirmState
import com.waseefakhtar.doseapp.feature.medicationconfirm.viewmodel.MedicationConfirmViewModel

@Composable
fun MedicationConfirmRoute(
    medication: List<Medication>?,
    onBackClicked: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MedicationConfirmViewModel = hiltViewModel()
) {
    val analyticsHelper = AnalyticsHelper.getInstance(LocalContext.current)
    medication?.let {
        MedicationConfirmScreen(it, viewModel, analyticsHelper, onBackClicked, navigateToHome)
    } ?: {
        FirebaseCrashlytics.getInstance().log("Error: Cannot show MedicationConfirmScreen. Medication is null.")
    }
}

@Composable
fun MedicationConfirmScreen(
    medications: List<Medication>,
    viewModel: MedicationConfirmViewModel,
    analyticsHelper: AnalyticsHelper,
    onBackClicked: () -> Unit,
    navigateToHome: () -> Unit
) {

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel
            .isMedicationSaved
            .collect {
                Toast.makeText(
                    context,
                    context.getString(
                        R.string.medication_timely_reminders_setup_message,
                        medications.first().name
                    ),
                    Toast.LENGTH_SHORT,
                ).show()
                navigateToHome()
                analyticsHelper.logEvent(AnalyticsEvents.MEDICATIONS_SAVED)
            }
    }

    Column(
        modifier = Modifier.padding(0.dp, 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FloatingActionButton(
            onClick = {
                analyticsHelper.logEvent(AnalyticsEvents.MEDICATION_CONFIRM_ON_BACK_CLICKED)
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
            text = stringResource(
                R.string.all_set,
                medication.dosage,
                medication.name,
                medication.recurrence.lowercase(),
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
                analyticsHelper.logEvent(AnalyticsEvents.MEDICATION_CONFIRM_ON_CONFIRM_CLICKED)
                viewModel.addMedication(context, MedicationConfirmState(medications))
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
