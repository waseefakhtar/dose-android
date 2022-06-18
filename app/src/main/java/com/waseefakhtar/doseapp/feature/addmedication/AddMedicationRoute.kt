package com.waseefakhtar.doseapp.feature.addmedication

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun AddMedicationRoute(
    modifier: Modifier = Modifier,
    //viewModel: CalendarViewModel = hiltViewModel()
) {
    AddMedicationScreen()
}

@Composable
fun AddMedicationScreen() {
    Text(
        text = "Coming Soon \uD83D\uDEA7",
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.displaySmall
    )
}