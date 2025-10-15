package dev.giuseppedarro.comanda.core.presentation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComandaTopAppBar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = { Text(title) },
        actions = actions
    )
}

@Composable
fun ComandaBottomBar(
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    BottomAppBar {
        IconButton(onClick = onHomeClick) {
            Icon(Icons.Default.Home, contentDescription = "Home")
        }
        // TODO: Add other bottom nav items
        IconButton(onClick = onProfileClick) {
            Icon(Icons.Default.Person, contentDescription = "Profile")
        }
    }
}

@Preview
@Composable
private fun ComandaTopAppBarPreview() {
    ComandaTheme {
        ComandaTopAppBar(
            title = "QuickOrder POS",
            actions = {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                }
            }
        )
    }
}

@Preview
@Composable
private fun ComandaBottomBarPreview() {
    ComandaTheme {
        ComandaBottomBar(onHomeClick = {}, onProfileClick = {})
    }
}
