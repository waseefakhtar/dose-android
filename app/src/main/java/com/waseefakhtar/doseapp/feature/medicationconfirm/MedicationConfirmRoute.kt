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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waseefakhtar.doseapp.R
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.extension.toFormattedString
import com.waseefakhtar.doseapp.feature.medicationconfirm.viewmodel.MedicationConfirmState
import com.waseefakhtar.doseapp.feature.medicationconfirm.viewmodel.MedicationConfirmViewModel

@Composable
fun MedicationConfirmRoute(
    medication: Medication?,
    onBackClicked: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MedicationConfirmViewModel = hiltViewModel()
) {
    medication?.let {
        MedicationConfirmScreen(it, viewModel, onBackClicked, navigateToHome)
    } ?: {
        // TODO: Show error and stay on AddMedication.
    }
}

@Composable
fun MedicationConfirmScreen(medication: Medication, viewModel: MedicationConfirmViewModel, onBackClicked: () -> Unit, navigateToHome: () -> Unit) {

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

        Text(
            text = stringResource(R.string.all_set, medication.dosage, medication.name, medication.recurrence.lowercase(), medication.endDate.toFormattedString()),
            style = MaterialTheme.typography.titleMedium
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                viewModel.addMedication(MedicationConfirmState(medication))
                navigateToHome()
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
