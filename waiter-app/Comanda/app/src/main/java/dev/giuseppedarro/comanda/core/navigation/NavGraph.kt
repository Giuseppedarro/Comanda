package dev.giuseppedarro.comanda.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.giuseppedarro.comanda.features.login.presentation.navigation.loginGraph
import dev.giuseppedarro.comanda.features.menu.presentation.navigation.menuGraph
import dev.giuseppedarro.comanda.features.orders.presentation.navigation.ordersGraph
import dev.giuseppedarro.comanda.features.printers.presentation.navigation.printersGraph
import dev.giuseppedarro.comanda.features.tables.presentation.navigation.tablesGraph

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Login) {
        loginGraph(navController)
        tablesGraph(navController) { navController.navigate(Printers) }
        ordersGraph(navController)
        printersGraph(navController)
        menuGraph(navController)
    }
}
