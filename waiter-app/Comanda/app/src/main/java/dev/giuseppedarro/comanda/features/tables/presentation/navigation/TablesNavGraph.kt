package dev.giuseppedarro.comanda.features.tables.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.giuseppedarro.comanda.core.navigation.FeatureGraph
import dev.giuseppedarro.comanda.features.orders.presentation.navigation.OrdersRoute
import dev.giuseppedarro.comanda.features.tables.presentation.TableOverviewScreen

fun NavGraphBuilder.tablesGraph(navController: NavController) {
    composable(FeatureGraph.Tables.route) {
        TableOverviewScreen(
            onNavigateToOrder = { tableNumber, numberOfPeople ->
                // This single callback handles navigation for both new and occupied tables.
                navController.navigate(OrdersRoute.MenuOrder.createRoute(tableNumber, numberOfPeople))
            }
        )
    }
}
