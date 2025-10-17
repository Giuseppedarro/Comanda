package dev.giuseppedarro.comanda.features.login.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.giuseppedarro.comanda.core.navigation.FeatureGraph
import dev.giuseppedarro.comanda.features.login.presentation.LoginScreen

fun NavGraphBuilder.loginGraph(navController: NavController) {
    composable(FeatureGraph.Login.route) {
        LoginScreen(
            onLoginSuccess = {
                navController.navigate(FeatureGraph.Tables.route) {
                    popUpTo(FeatureGraph.Login.route) { inclusive = true }
                }
            }
        )
    }
}
