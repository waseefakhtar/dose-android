package com.waseefakhtar.doseapp.feature.calendar.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.waseefakhtar.doseapp.core.navigation.DoseNavigationDestination
import com.waseefakhtar.doseapp.feature.calendar.CalendarRoute

object CalendarDestination : DoseNavigationDestination {
    override val route = "calendar_route"
    override val destination = "calendar_destination"
}

fun NavGraphBuilder.calendarGraph() {
    composable(route = CalendarDestination.route) {
        CalendarRoute()
    }
}