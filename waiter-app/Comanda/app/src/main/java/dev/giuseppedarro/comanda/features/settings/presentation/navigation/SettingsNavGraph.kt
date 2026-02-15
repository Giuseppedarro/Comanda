package dev.giuseppedarro.comanda.features.settings.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.giuseppedarro.comanda.navigation.Login
import dev.giuseppedarro.comanda.navigation.Settings
import dev.giuseppedarro.comanda.features.settings.presentation.ManageUsersScreen
import dev.giuseppedarro.comanda.features.settings.presentation.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
private object SettingsMainRoute

@Serializable
private object ManageUsersRoute

fun NavGraphBuilder.settingsNavGraph(navController: NavController) {
    navigation<Settings>(
        startDestination = SettingsMainRoute,
    ) {
        composable<SettingsMainRoute> {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Login) {
                        popUpTo<Settings> { inclusive = true }
                    }
                },
                onManageUsersClick = { navController.navigate(ManageUsersRoute) }
            )
        }
        composable<ManageUsersRoute> {
            ManageUsersScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
