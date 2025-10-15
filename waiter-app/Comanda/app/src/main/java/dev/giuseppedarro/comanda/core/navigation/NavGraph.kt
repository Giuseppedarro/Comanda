package dev.giuseppedarro.comanda.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.giuseppedarro.comanda.features.login.presentation.LoginScreen
import dev.giuseppedarro.comanda.features.orders.presentation.MenuOrderScreen
import dev.giuseppedarro.comanda.features.tables.presentation.TableOverviewScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { 
                    navController.navigate(Screen.TableOverview.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.TableOverview.route) {
            TableOverviewScreen(
                onTableClick = { 
                    // In the future, you can pass the table number as an argument
                    navController.navigate(Screen.MenuOrder.route)
                },
                onSettingsClick = { /* TODO: Navigate to settings */ },
                onHomeClick = { /* TODO: Navigate to home */ },
                onProfileClick = { /* TODO: Navigate to profile */ }
            )
        }
        composable(Screen.MenuOrder.route) {
            MenuOrderScreen(
                onProceedClick = { /* TODO: Navigate to order summary */ },
                onBillOverviewClick = { /* TODO: Navigate to bill overview */ }
            )
        }
    }
}
