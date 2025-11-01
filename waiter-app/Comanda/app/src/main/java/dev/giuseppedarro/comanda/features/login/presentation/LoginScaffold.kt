package dev.giuseppedarro.comanda.features.login.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

internal sealed class LoginScreenDestination(val route: String, val label: String, val icon: ImageVector) {
    object Login : LoginScreenDestination("login_screen", "Login", Icons.Default.Lock)
    object ServerAddress : LoginScreenDestination("server_address_screen", "Server", Icons.Default.Dns)
}

internal val LOGIN_SCREENS = listOf(
    LoginScreenDestination.Login,
    LoginScreenDestination.ServerAddress
)

@Composable
internal fun LoginScaffold(
    currentRoute: String?,
    onNavigate: (route: String) -> Unit,
    content: @Composable (padding: PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                LOGIN_SCREENS.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentRoute == screen.route,
                        onClick = { onNavigate(screen.route) }
                    )
                }
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}
