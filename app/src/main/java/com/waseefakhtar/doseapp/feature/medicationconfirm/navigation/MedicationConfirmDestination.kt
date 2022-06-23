package com.waseefakhtar.doseapp.feature.medicationconfirm.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.waseefakhtar.doseapp.core.navigation.DoseNavigationDestination
import com.waseefakhtar.doseapp.domain.model.AssetParamType
import com.waseefakhtar.doseapp.domain.model.Medication
import com.waseefakhtar.doseapp.feature.medicationconfirm.MedicationConfirmRoute

private const val MEDICATION = "medication"

object MedicationConfirmDestination : DoseNavigationDestination {
    override val route = "medication_confirm_route"
    override val destination = "medication_confirm_destination"
}

fun NavGraphBuilder.medicationConfirmGraph(bottomBarVisibility: MutableState<Boolean>, fabVisibility: MutableState<Boolean>, onBackClicked: () -> Unit, navigateToHome: () -> Unit) {

    // TODO: Fix repeat calls on Home when this composable is uncommented.
    /*composable(
        route = "${MedicationConfirmDestination.route}/${MEDICATION}",
        arguments = listOf(
            navArgument(MEDICATION) {
                type = AssetParamType()
            }
        )) {
        LaunchedEffect(null) {
            bottomBarVisibility.value = false
            fabVisibility.value = false
        }

        val medication = it.arguments?.getParcelable<Medication>(MEDICATION)
        MedicationConfirmRoute(medication, onBackClicked, navigateToHome)
    }*/
}