package com.waseefakhtar.doseapp.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.waseefakhtar.doseapp.R
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.extension.hasPassed
import com.waseefakhtar.doseapp.extension.toFormattedDateString
import com.waseefakhtar.doseapp.extension.toFormattedTimeString
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationCard(
    medication: Medication,
    navigateToMedicationDetail: (Medication) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        onClick = { navigateToMedicationDetail(medication) },
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .weight(2f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = MaterialTheme.typography.titleSmall,
                    text = medication.medicationTime.toFormattedDateString().uppercase(),
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = medication.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )

                val medicationStatusText = when {
                    medication.medicationTime.hasPassed() -> {
                        if (medication.medicationTaken) {
                            stringResource(
                                id = R.string.medication_taken_at,
                                medication.medicationTime.toFormattedTimeString()
                            )
                        } else {
                            stringResource(
                                id = R.string.medication_skipped_at,
                                medication.medicationTime.toFormattedTimeString()
                            )
                        }
                    }

                    else -> stringResource(
                        id = R.string.medication_scheduled_at,
                        medication.medicationTime.toFormattedTimeString()
                    )
                }

                Text(
                    text = medicationStatusText,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
        }
    }
}

@Preview
@Composable
private fun MedicationCardTakeNowPreview() {
    MedicationCard(
        Medication(
            id = 123L,
            name = "A big big name for a little medication I needs to take",
            dosage = 1,
            recurrence = "2",
            endDate = Date(),
            medicationTime = Date(),
            medicationTaken = false
        )
    ) { }
}

@Preview
@Composable
private fun MedicationCardTakenPreview() {
    MedicationCard(
        Medication(
            id = 123L,
            name = "A big big name for a little medication I needs to take",
            dosage = 1,
            recurrence = "2",
            endDate = Date(),
            medicationTime = Date(),
            medicationTaken = true
        )
    ) { }
}
