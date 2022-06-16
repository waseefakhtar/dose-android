package com.waseefakhtar.doseapp.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.waseefakhtar.doseapp.core.navigation.DoseNavigationDestination
import com.waseefakhtar.doseapp.feature.home.HomeRoute

object HomeDestination : DoseNavigationDestination {
    override val route = "home_route"
    override val destination = "home_destination"
}

fun NavGraphBuilder.homeGraph() {
    composable(route = HomeDestination.route) {
        HomeRoute()
    }
}