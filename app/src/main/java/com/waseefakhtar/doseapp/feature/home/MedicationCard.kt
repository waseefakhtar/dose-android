package com.waseefakhtar.doseapp.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.util.getTimeRemaining
import java.util.Date

@Composable
fun MedicationCard(
    medication: Medication,
    navigateToMedicationDetail: (Medication) -> Unit,
    onTakeButtonClicked: (Medication) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navigateToMedicationDetail(medication)
            },
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = medication.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = medication.timesOfDay.joinToString(", ")
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = getTimeRemaining(medication),
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                onClick = { onTakeButtonClicked(medication) },
                enabled = !medication.medicationTaken
            ) {
                if (medication.medicationTaken) {
                    Text(
                        text = "Taken"
                    )
                } else {
                    Text(
                        text = "Take now"
                    )
                }
            }
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
            timesOfDay = listOf(),
            medicationTaken = false,
            date = Date(),
        ), { }
    ) {
    }
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
            timesOfDay = listOf(),
            medicationTaken = true,
            date = Date(),
        ), { }
    ) {
    }
}
