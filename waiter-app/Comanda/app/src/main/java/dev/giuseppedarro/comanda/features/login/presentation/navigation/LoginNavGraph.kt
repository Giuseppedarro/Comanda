package dev.giuseppedarro.comanda.features.login.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import dev.giuseppedarro.comanda.core.navigation.FeatureGraph
import dev.giuseppedarro.comanda.features.login.presentation.LoginScaffold
import dev.giuseppedarro.comanda.features.login.presentation.LoginScreen
import dev.giuseppedarro.comanda.features.login.presentation.LoginScreenDestination
import dev.giuseppedarro.comanda.features.login.presentation.ServerAddressScreen

fun NavGraphBuilder.loginGraph(navController: NavController, onLoginSuccess: () -> Unit) {
    navigation(
        startDestination = LoginScreenDestination.Login.route,
        route = FeatureGraph.Login.route
    ) {
        composable(LoginScreenDestination.Login.route) {
            LoginScaffoldWrapper(navController) {
                LoginScreen(onLoginSuccess = onLoginSuccess)
            }
        }

        composable(LoginScreenDestination.ServerAddress.route) {
            LoginScaffoldWrapper(navController) {
                ServerAddressScreen()
            }
        }
    }
}

@Composable
private fun LoginScaffoldWrapper(
    navController: NavController,
    content: @Composable () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LoginScaffold(
        currentRoute = currentRoute,
        onNavigate = { route ->
            navController.navigate(route) {
                // Avoid multiple copies of the same destination when re-selecting the same item
                launchSingleTop = true
                // Restore state when re-selecting a previously selected item
                restoreState = true
            }
        },
        content = { content() }
    )
}
