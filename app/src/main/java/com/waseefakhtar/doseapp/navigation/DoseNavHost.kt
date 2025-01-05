package com.waseefakhtar.doseapp.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.waseefakhtar.doseapp.feature.addmedication.navigation.addMedicationGraph
import com.waseefakhtar.doseapp.feature.calendar.navigation.calendarGraph
import com.waseefakhtar.doseapp.feature.history.historyGraph
import com.waseefakhtar.doseapp.feature.home.navigation.HomeDestination
import com.waseefakhtar.doseapp.feature.home.navigation.homeGraph
import com.waseefakhtar.doseapp.feature.medicationconfirm.navigation.MEDICATION
import com.waseefakhtar.doseapp.feature.medicationconfirm.navigation.MedicationConfirmDestination
import com.waseefakhtar.doseapp.feature.medicationconfirm.navigation.medicationConfirmGraph
import com.waseefakhtar.doseapp.feature.medicationdetail.MedicationDetailDestination
import com.waseefakhtar.doseapp.feature.medicationdetail.medicationDetailGraph
import com.waseefakhtar.doseapp.util.navigateSingleTop

@Composable
fun DoseNavHost(
    bottomBarVisibility: MutableState<Boolean>,
    fabVisibility: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = HomeDestination.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        homeGraph(
            navController = navController,
            bottomBarVisibility = bottomBarVisibility,
            fabVisibility = fabVisibility,
            navigateToMedicationDetail = { medication ->
                navController.navigate(
                    MedicationDetailDestination.createNavigationRoute(medication.id)
                )
            }
        )
        historyGraph(
            bottomBarVisibility = bottomBarVisibility,
            fabVisibility = fabVisibility,
            navigateToMedicationDetail = { medication ->
                navController.navigate(
                    MedicationDetailDestination.createNavigationRoute(medication.id)
                )
            }
        )
        medicationDetailGraph(
            bottomBarVisibility = bottomBarVisibility,
            fabVisibility = fabVisibility,
            onBackClicked = { navController.navigateUp() }
        )
        calendarGraph(bottomBarVisibility, fabVisibility)
        addMedicationGraph(
            navController = navController,
            bottomBarVisibility = bottomBarVisibility,
            fabVisibility = fabVisibility,
            onBackClicked = { navController.navigateUp() },
            navigateToMedicationConfirm = {
                // TODO: Replace with medication id
                val bundle = Bundle()
                bundle.putParcelableArrayList(MEDICATION, ArrayList(it))
                navController.currentBackStackEntry?.savedStateHandle.apply {
                    this?.set(MEDICATION, bundle)
                }
                navController.navigate(MedicationConfirmDestination.route)
            }
        )
        medicationConfirmGraph(
            navController = navController,
            bottomBarVisibility = bottomBarVisibility,
            fabVisibility = fabVisibility,
            onBackClicked = { navController.navigateUp() },
            navigateToHome = {
                navController.navigateSingleTop(HomeDestination.route)
            }
        )
    }
}
