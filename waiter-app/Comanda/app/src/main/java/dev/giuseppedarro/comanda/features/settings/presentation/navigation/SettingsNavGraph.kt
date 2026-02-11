package dev.giuseppedarro.comanda.features.settings.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.giuseppedarro.comanda.core.navigation.Login
import dev.giuseppedarro.comanda.core.navigation.Settings
import dev.giuseppedarro.comanda.features.settings.presentation.SettingsScreen

fun NavGraphBuilder.settingsNavGraph(navController: NavController) {
    composable<Settings> {
        SettingsScreen(
            onBackClick = { navController.popBackStack() },
            onLogout = {
                navController.navigate(Login) {
                    popUpTo(Settings) { inclusive = true }
                }
            }
        )
    }
}
