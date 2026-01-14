package dev.giuseppedarro.comanda.features.printers.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.giuseppedarro.comanda.core.navigation.Printers
import dev.giuseppedarro.comanda.features.printers.presentation.PrinterManagementScreen
import org.koin.compose.koinInject

fun NavGraphBuilder.printersGraph() {
    composable<Printers> {
        PrinterManagementScreen(
            viewModel = koinInject()
        )
    }
}