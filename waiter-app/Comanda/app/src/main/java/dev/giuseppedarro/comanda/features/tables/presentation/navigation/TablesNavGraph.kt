package dev.giuseppedarro.comanda.features.tables.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.giuseppedarro.comanda.core.navigation.Login
import dev.giuseppedarro.comanda.core.navigation.Tables
import dev.giuseppedarro.comanda.features.orders.presentation.navigation.MenuOrder
import dev.giuseppedarro.comanda.features.tables.presentation.TableOverviewScreen

fun NavGraphBuilder.tablesGraph(
    navController: NavController,
    onNavigateToPrinters: () -> Unit
) {
    composable<Tables> {
        TableOverviewScreen(
            onNavigateToOrder = { tableNumber, numberOfPeople ->
                navController.navigate(MenuOrder(tableNumber, numberOfPeople))
            },
            onNavigateToPrinters = onNavigateToPrinters,
            onNavigateToMenu = { /* TODO: Implement menu navigation */ },
            onNavigateToSettings = { /* TODO: Implement settings navigation */ },
            onLogout = {
                // Navigate back to login and clear the back stack
                navController.navigate(Login) {
                    popUpTo<Tables> { inclusive = true }
                }
            }
        )
    }
}
