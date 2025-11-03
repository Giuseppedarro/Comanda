package dev.giuseppedarro.comanda.features.tables.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.giuseppedarro.comanda.core.navigation.MenuOrder
import dev.giuseppedarro.comanda.core.navigation.Tables
import dev.giuseppedarro.comanda.features.tables.presentation.TableOverviewScreen

fun NavGraphBuilder.tablesGraph(navController: NavController) {
    composable<Tables> {
        TableOverviewScreen(
            onNavigateToOrder = { tableNumber, numberOfPeople ->
                navController.navigate(MenuOrder(tableNumber, numberOfPeople))
            }
        )
    }
}
