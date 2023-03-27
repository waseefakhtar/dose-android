package com.waseefakhtar.doseapp.feature.addmedication.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.waseefakhtar.doseapp.core.navigation.DoseNavigationDestination
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.feature.addmedication.AddMedicationRoute

object AddMedicationDestination : DoseNavigationDestination {
    override val route = "add_medication_route"
    override val destination = "add_medication_destination"
}

fun NavGraphBuilder.addMedicationGraph(bottomBarVisibility: MutableState<Boolean>, fabVisibility: MutableState<Boolean>, onBackClicked: () -> Unit, navigateToMedicationConfirm: (List<Medication>) -> Unit) {
    composable(route = AddMedicationDestination.route) {
        LaunchedEffect(null) {
            bottomBarVisibility.value = false
            fabVisibility.value = false
        }
        AddMedicationRoute(onBackClicked, navigateToMedicationConfirm)
    }
}
