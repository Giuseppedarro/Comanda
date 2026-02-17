package dev.giuseppedarro.comanda.features.login.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.giuseppedarro.comanda.features.login.presentation.LoginScreen
import kotlinx.serialization.Serializable

@Serializable
object Login

fun NavGraphBuilder.loginGraph(onLoginSuccess: () -> Unit) {
    composable<Login> {
        LoginScreen(
            onLoginSuccess = onLoginSuccess
        )
    }
}
