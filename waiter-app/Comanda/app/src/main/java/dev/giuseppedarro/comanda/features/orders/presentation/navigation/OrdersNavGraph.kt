package dev.giuseppedarro.comanda.features.orders.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import dev.giuseppedarro.comanda.core.navigation.Orders
import dev.giuseppedarro.comanda.features.orders.presentation.MenuOrderScreen

fun NavGraphBuilder.ordersGraph(navController: NavController) {
    navigation<Orders>(startDestination = MenuOrder(tableNumber = 0, numberOfPeople = 0)) {
        composable<MenuOrder> { backStackEntry ->
            val args = backStackEntry.toRoute<MenuOrder>()
            MenuOrderScreen(
                tableNumber = args.tableNumber,
                numberOfPeople = args.numberOfPeople,
                onProceedClick = { /* TODO: Navigate to order summary */ },
                onBillOverviewClick = { /* TODO: Navigate to bill overview */ }
            )
        }
    }
}
