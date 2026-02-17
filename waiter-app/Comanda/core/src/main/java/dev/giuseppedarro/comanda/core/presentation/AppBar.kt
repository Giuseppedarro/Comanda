package dev.giuseppedarro.comanda.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.core.ui.theme.ComandaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComandaTopAppBar(
    title: String,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Restaurant,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        },
        navigationIcon = navigationIcon,
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
            title = "Comanda",
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
