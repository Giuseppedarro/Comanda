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
            onTableClick = {
                // For occupied tables, we would need to know the order ID to modify it.
                // For now, let's just navigate to the orders graph.
                // In the future, this would be something like:
                // navController.navigate(OrdersRoute.ModifyOrder.createRoute(it.orderId))
                navController.navigate(FeatureGraph.Orders.route) // This is a placeholder
            },
            onNewOrderClick = { tableNumber, numberOfPeople ->
                // For new tables, we construct the route with the required arguments.
                navController.navigate(OrdersRoute.MenuOrder.createRoute(tableNumber, numberOfPeople))
            }
        )
    }
}