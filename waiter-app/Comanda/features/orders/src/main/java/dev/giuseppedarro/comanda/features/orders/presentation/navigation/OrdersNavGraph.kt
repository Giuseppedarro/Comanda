package dev.giuseppedarro.comanda.features.orders.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import dev.giuseppedarro.comanda.features.orders.presentation.MenuOrderScreen
import kotlinx.serialization.Serializable

@Serializable
object Orders

fun NavGraphBuilder.ordersGraph(onSendClick: () -> Unit) {
    navigation<Orders>(startDestination = MenuOrder(tableNumber = 0, numberOfPeople = 0)) {
        composable<MenuOrder> { backStackEntry ->
            val args = backStackEntry.toRoute<MenuOrder>()
            MenuOrderScreen(
                tableNumber = args.tableNumber,
                numberOfPeople = args.numberOfPeople,
                onSendClick = onSendClick
            )
        }
    }
}
