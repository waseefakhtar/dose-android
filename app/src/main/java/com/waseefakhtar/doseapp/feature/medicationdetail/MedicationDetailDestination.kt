package com.waseefakhtar.doseapp.feature.medicationdetail

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.waseefakhtar.doseapp.core.navigation.DoseNavigationDestination
import com.waseefakhtar.doseapp.core.navigation.NavigationConstants.DEEP_LINK_URI_PATTERN
import com.waseefakhtar.doseapp.core.navigation.NavigationConstants.MEDICATION_ID

object MedicationDetailDestination : DoseNavigationDestination {
    override val route = "medication_detail_route/{$MEDICATION_ID}"
    override val destination = "medication_detail_destination"

    fun createNavigationRoute(medicationId: Long) = "medication_detail_route/$medicationId"
}

fun NavGraphBuilder.medicationDetailGraph(
    bottomBarVisibility: MutableState<Boolean>,
    fabVisibility: MutableState<Boolean>,
    onBackClicked: () -> Unit
) {
    composable(
        route = MedicationDetailDestination.route,
        arguments = listOf(
            navArgument(MEDICATION_ID) { type = NavType.LongType }
        ),
        deepLinks = listOf(
            navDeepLink {
                uriPattern = DEEP_LINK_URI_PATTERN
            }
        )
    ) { backStackEntry ->
        LaunchedEffect(null) {
            bottomBarVisibility.value = false
            fabVisibility.value = false
        }

        val medicationId = backStackEntry.arguments?.getLong(MEDICATION_ID)

        MedicationDetailRoute(
            medicationId = medicationId,
            onBackClicked = onBackClicked
        )
    }
}
