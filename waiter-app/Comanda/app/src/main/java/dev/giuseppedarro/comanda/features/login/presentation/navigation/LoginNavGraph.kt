package dev.giuseppedarro.comanda.features.login.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.giuseppedarro.comanda.navigation.Login
import dev.giuseppedarro.comanda.navigation.Tables
import dev.giuseppedarro.comanda.features.login.presentation.LoginScreen

fun NavGraphBuilder.loginGraph(navController: NavController) {
    composable<Login> {
        LoginScreen(
            onLoginSuccess = {
                navController.navigate(Tables) {
                    popUpTo<Login> { inclusive = true }
                }
            }
        )
    }
}
