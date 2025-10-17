package dev.giuseppedarro.comanda.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.giuseppedarro.comanda.features.login.presentation.navigation.loginGraph
import dev.giuseppedarro.comanda.features.orders.presentation.navigation.ordersGraph
import dev.giuseppedarro.comanda.features.tables.presentation.navigation.tablesGraph

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = FeatureGraph.Login.route) {
        loginGraph(navController)
        tablesGraph(navController)
        ordersGraph(navController)
    }
}
