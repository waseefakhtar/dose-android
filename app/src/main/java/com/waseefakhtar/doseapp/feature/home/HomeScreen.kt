package com.waseefakhtar.doseapp.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    //viewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreen()
}

@Composable
fun HomeScreen() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Greeting()
        DailyOverview()
    }
}

@Composable
fun Greeting() {
    Column {
        // TODO: Add greeting based on time of day e.g. Good Morning, Good Afternoon, Good evening.
        // TODO: Get name from DB and show user's first name.
        Text(
            text = "Good",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displayMedium
        )
        Text(
            text = "Morning!",
            style = MaterialTheme.typography.displayMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyOverview() {

    Spacer(modifier = Modifier.padding(8.dp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(36.dp),
        colors = cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer ,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {


    }

}