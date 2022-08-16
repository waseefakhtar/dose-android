package com.waseefakhtar.doseapp.feature.calendar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun CalendarRoute(
    modifier: Modifier = Modifier,
    // viewModel: CalendarViewModel = hiltViewModel()
) {
    CalendarScreen()
}

@Composable
fun CalendarScreen() {
    Text(
        text = "Coming Soon \uD83D\uDEA7",
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.displaySmall
    )
}
