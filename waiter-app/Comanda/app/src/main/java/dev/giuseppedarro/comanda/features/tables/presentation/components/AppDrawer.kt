package dev.giuseppedarro.comanda.features.tables.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    onNavigateToPrinters: () -> Unit,
    onNavigateToMenu: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(280.dp),
        windowInsets = WindowInsets(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
            Column(modifier = Modifier.padding(16.dp)) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "GD",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Giuseppe D'''Arrò",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
        Divider()
        NavigationDrawerItem(
            label = { Text("Printers") },
            icon = { Icon(Icons.Default.Print, contentDescription = "Printers") },
            selected = false,
            onClick = {
                onNavigateToPrinters()
                onCloseDrawer()
            }
        )
        NavigationDrawerItem(
            label = { Text("Menu") },
            icon = { Icon(Icons.Default.RestaurantMenu, contentDescription = "Menu") },
            selected = false,
            onClick = {
                onNavigateToMenu()
                onCloseDrawer()
            }
        )
        NavigationDrawerItem(
            label = { Text("Settings (to be implemented)") },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            selected = false,
            onClick = {
                onNavigateToSettings()
                onCloseDrawer()
            }
        )
        NavigationDrawerItem(label = { Text("Logout") },
            icon = { Icon(Icons.Default.Logout, contentDescription = "Logout") },
            selected = false,
            onClick = {
                onLogout()
                onCloseDrawer()
            }
        )
    }
}

@Preview
@Composable
fun AppDrawerPreview() {
    ComandaTheme {
        AppDrawer(
            onNavigateToPrinters = {},
            onNavigateToMenu = {},
            onNavigateToSettings = {},
            onLogout = {},
            onCloseDrawer = {}
        )
    }
}