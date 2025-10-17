package dev.giuseppedarro.comanda.features.orders.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.giuseppedarro.comanda.core.navigation.FeatureGraph
import dev.giuseppedarro.comanda.features.orders.presentation.MenuOrderScreen

fun NavGraphBuilder.ordersGraph(navController: NavController) {
    navigation(
        startDestination = OrdersRoute.MenuOrder.route,
        route = FeatureGraph.Orders.route
    ) {
        composable(OrdersRoute.MenuOrder.route) {
            MenuOrderScreen(
                onProceedClick = { /* TODO: Navigate to order summary */ },
                onBillOverviewClick = { /* TODO: Navigate to bill overview */ }
            )
        }
    }
}
