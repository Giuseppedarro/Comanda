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
        route = FeatureGraph.Orders.route // The route for the whole orders graph
    ) {
        composable(
            route = OrdersRoute.MenuOrder.route,
            arguments = OrdersRoute.MenuOrder.arguments
        ) {
            val tableNumber = it.arguments?.getInt("tableNumber") ?: 0
            val numberOfPeople = it.arguments?.getInt("numberOfPeople") ?: 0
            MenuOrderScreen(
                tableNumber = tableNumber,
                numberOfPeople = numberOfPeople,
                onOrderSent = { navController.popBackStack() } // Navigate back
            )
        }
    }
}
