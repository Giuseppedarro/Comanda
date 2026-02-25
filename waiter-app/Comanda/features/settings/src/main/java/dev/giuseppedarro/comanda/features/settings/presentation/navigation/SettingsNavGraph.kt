package dev.giuseppedarro.comanda.features.settings.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.giuseppedarro.comanda.features.settings.presentation.ManageUsersScreen
import dev.giuseppedarro.comanda.features.settings.presentation.SettingsScreen
import dev.giuseppedarro.comanda.features.settings.presentation.ThemeSettingsScreen
import kotlinx.serialization.Serializable

@Serializable
object Settings

@Serializable
object SettingsMainRoute

@Serializable
object ManageUsersRoute

@Serializable
object ThemeSettingsRoute

fun NavGraphBuilder.settingsNavGraph(
    onBackClick: () -> Unit,
    onLogout: () -> Unit,
    onManageUsersClick: () -> Unit,
    onThemeSettingsClick: () -> Unit
) {
    navigation<Settings>(
        startDestination = SettingsMainRoute,
    ) {
        composable<SettingsMainRoute> {
            SettingsScreen(
                onBackClick = onBackClick,
                onLogout = onLogout,
                onManageUsersClick = onManageUsersClick,
                onThemeSettingsClick = onThemeSettingsClick
            )
        }
        composable<ManageUsersRoute> {
            ManageUsersScreen(
                onBackClick = onBackClick
            )
        }
        composable<ThemeSettingsRoute> {
            ThemeSettingsScreen(
                onBackClick = onBackClick
            )
        }
    }
}
