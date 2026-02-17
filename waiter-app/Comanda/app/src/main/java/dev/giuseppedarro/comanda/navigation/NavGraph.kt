package dev.giuseppedarro.comanda.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.giuseppedarro.comanda.features.login.presentation.navigation.Login
import dev.giuseppedarro.comanda.features.login.presentation.navigation.loginGraph
import dev.giuseppedarro.comanda.features.menu.presentation.navigation.Category
import dev.giuseppedarro.comanda.features.menu.presentation.navigation.Menu
import dev.giuseppedarro.comanda.features.menu.presentation.navigation.menuGraph
import dev.giuseppedarro.comanda.features.orders.presentation.navigation.MenuOrder
import dev.giuseppedarro.comanda.features.orders.presentation.navigation.ordersGraph
import dev.giuseppedarro.comanda.features.printers.presentation.navigation.Printers
import dev.giuseppedarro.comanda.features.printers.presentation.navigation.printersGraph
import dev.giuseppedarro.comanda.features.settings.presentation.navigation.ManageUsersRoute
import dev.giuseppedarro.comanda.features.settings.presentation.navigation.Settings
import dev.giuseppedarro.comanda.features.settings.presentation.navigation.settingsNavGraph
import dev.giuseppedarro.comanda.features.tables.presentation.navigation.Tables
import dev.giuseppedarro.comanda.features.tables.presentation.navigation.tablesGraph

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Login) {
        loginGraph {
            navController.navigate(Tables) {
                popUpTo<Login> { inclusive = true }
            }
        }
        tablesGraph(
            onNavigateToOrder = { tableNumber, numberOfPeople ->
                navController.navigate(MenuOrder(tableNumber, numberOfPeople))
            },
            onNavigateToPrinters = { navController.navigate(Printers) },
            onNavigateToMenu = { navController.navigate(Menu) },
            onNavigateToSettings = { navController.navigate(Settings) },
            onLogout = {
                navController.navigate(Login) {
                    popUpTo<Tables> { inclusive = true }
                }
            }
        )
        ordersGraph(
            onSendClick = {
                navController.navigate(Tables)
            }
        )
        printersGraph(onNavigateBack = { navController.popBackStack() })
        menuGraph(
            onNavigateToCategory = { categoryName ->
                navController.navigate(Category(categoryName))
            },
            onNavigateBack = { navController.popBackStack() }
        )
        settingsNavGraph(
            onBackClick = { navController.popBackStack() },
            onLogout = {
                navController.navigate(Login) {
                    popUpTo<Settings> { inclusive = true }
                }
            },
            onManageUsersClick = { navController.navigate(ManageUsersRoute) }
        )
    }
}
