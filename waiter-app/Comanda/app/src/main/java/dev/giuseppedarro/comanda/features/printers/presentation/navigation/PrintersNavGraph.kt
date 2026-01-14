package dev.giuseppedarro.comanda.features.printers.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.giuseppedarro.comanda.core.navigation.Printers
import dev.giuseppedarro.comanda.features.printers.presentation.PrinterManagementScreen

fun NavGraphBuilder.printersGraph(navController: NavController) {
    composable<Printers> {
        PrinterManagementScreen(
            onNavigateBack = { navController.popBackStack() }
        )
    }
}
