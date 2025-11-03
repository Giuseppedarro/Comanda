package dev.giuseppedarro.comanda.features.orders.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.giuseppedarro.comanda.core.navigation.MenuOrder
import dev.giuseppedarro.comanda.features.orders.presentation.MenuOrderScreen

fun NavGraphBuilder.ordersGraph(navController: NavController) {
    composable<MenuOrder> { backStackEntry ->
        val args = backStackEntry.toRoute<MenuOrder>()
        MenuOrderScreen(
            tableNumber = args.tableNumber,
            numberOfPeople = args.numberOfPeople,
            onOrderSent = { navController.popBackStack() }
        )
    }
}
