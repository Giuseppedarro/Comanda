package dev.giuseppedarro.comanda.features.tables.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.giuseppedarro.comanda.features.tables.presentation.TableOverviewScreen
import kotlinx.serialization.Serializable

@Serializable
object Tables

fun NavGraphBuilder.tablesGraph(
    onNavigateToOrder: (tableNumber: Int, numberOfPeople: Int) -> Unit,
    onNavigateToPrinters: () -> Unit,
    onNavigateToMenu: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit
) {
    composable<Tables> {
        TableOverviewScreen(
            onNavigateToOrder = onNavigateToOrder,
            onNavigateToPrinters = onNavigateToPrinters,
            onNavigateToMenu = onNavigateToMenu,
            onNavigateToSettings = onNavigateToSettings,
            onLogout = onLogout
        )
    }
}
