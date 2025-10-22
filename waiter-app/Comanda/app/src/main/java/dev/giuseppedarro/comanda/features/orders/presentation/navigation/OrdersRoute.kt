package dev.giuseppedarro.comanda.features.orders.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class OrdersRoute(val route: String, val arguments: List<NamedNavArgument> = emptyList()) {
    object MenuOrder : OrdersRoute(
        route = "menu_order/{tableNumber}/{numberOfPeople}",
        arguments = listOf(
            navArgument("tableNumber") { type = NavType.IntType },
            navArgument("numberOfPeople") { type = NavType.IntType }
        )
    ) {
        fun createRoute(tableNumber: Int, numberOfPeople: Int) = "menu_order/$tableNumber/$numberOfPeople"
    }
}
