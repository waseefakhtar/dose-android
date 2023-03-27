package com.waseefakhtar.doseapp.feature.medicationconfirm.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.waseefakhtar.doseapp.core.navigation.DoseNavigationDestination
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.feature.medicationconfirm.MedicationConfirmRoute

const val MEDICATION = "medication"

object MedicationConfirmDestination : DoseNavigationDestination {
    override val route = "medication_confirm_route"
    override val destination = "medication_confirm_destination"
}

fun NavGraphBuilder.medicationConfirmGraph(navController: NavController, bottomBarVisibility: MutableState<Boolean>, fabVisibility: MutableState<Boolean>, onBackClicked: () -> Unit, navigateToHome: () -> Unit) {

    composable(
        route = MedicationConfirmDestination.route,
    ) {
        LaunchedEffect(null) {
            bottomBarVisibility.value = false
            fabVisibility.value = false
        }
        val medication = navController.previousBackStackEntry?.arguments?.getParcelableArrayList<Medication>(MEDICATION)
        MedicationConfirmRoute(medication, onBackClicked, navigateToHome)
    }
}
