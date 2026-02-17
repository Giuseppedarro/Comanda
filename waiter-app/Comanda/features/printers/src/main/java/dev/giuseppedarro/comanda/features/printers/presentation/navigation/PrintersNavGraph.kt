package dev.giuseppedarro.comanda.features.printers.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.giuseppedarro.comanda.features.printers.presentation.PrinterManagementScreen
import kotlinx.serialization.Serializable

@Serializable
object Printers

fun NavGraphBuilder.printersGraph(onNavigateBack: () -> Unit) {
    composable<Printers> {
        PrinterManagementScreen(
            onNavigateBack = onNavigateBack
        )
    }
}
