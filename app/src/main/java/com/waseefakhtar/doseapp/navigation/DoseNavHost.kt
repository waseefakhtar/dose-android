package com.waseefakhtar.doseapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.waseefakhtar.doseapp.feature.home.navigation.HomeDestination
import com.waseefakhtar.doseapp.feature.home.navigation.homeGraph

@Composable
fun DoseNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = HomeDestination.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        homeGraph()
    }
}