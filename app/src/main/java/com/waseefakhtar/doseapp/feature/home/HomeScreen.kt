package com.waseefakhtar.doseapp.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    //viewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreen()
}

@Composable
fun HomeScreen() {
    Greeting("Android")
}

@Composable
fun Greeting(name: String) {
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