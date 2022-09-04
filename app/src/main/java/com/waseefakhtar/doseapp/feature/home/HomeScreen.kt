package com.waseefakhtar.doseapp.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.waseefakhtar.doseapp.R
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.util.Recurrence
import com.waseefakhtar.doseapp.util.TimesOfDay
import java.util.Date

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    // viewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreen()
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Greeting()
        DailyOverview()
        DailyReview()
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
fun DailyOverview() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(36.dp),
        colors = cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.tertiary
        )
    ) {

        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(24.dp, 24.dp, 0.dp, 16.dp)
                    .fillMaxWidth(.36F),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = "Your plan for today",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = "0 of 1 completed",
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

@Composable
fun DailyReview() {

    Text(
        modifier = Modifier
            .padding(4.dp, 12.dp, 8.dp, 0.dp)
            .fillMaxWidth(),
        text = "Today".uppercase(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium,
    )

    // Demo Medication Card
    // TODO: Remove when we retrieve data from DB.
    val medication = Medication(
        name = "Hexamine",
        dosage = 2,
        recurrence = Recurrence.Daily.name,
        endDate = Date(),
        timesOfDay = listOf(TimesOfDay.Morning, TimesOfDay.Night),
    )

    MedicationCard(medication)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationCard(medication: Medication) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(30.dp),
        colors = cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {

        Row(
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = medication.name,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Next dose in 2 mins"
                )
            }

            Row(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalArrangement = Arrangement.End
            ) {

                Button(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = {
                        // TODO: Mark medication as taken
                        // TODO: Update DB with medication as taken and store with time.
                        // TODO: Cross the name of the token and disable the button with text "Taken"
                    }
                ) {

                    Text(
                        text = "Take now"
                    )
                }
            }
        }
    }
}
