package dev.giuseppedarro.comanda.features.settings.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.R
import dev.giuseppedarro.comanda.core.presentation.ComandaTopAppBar
import dev.giuseppedarro.comanda.core.ui.theme.ComandaTheme

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onLogout: () -> Unit,
    onManageUsersClick: () -> Unit
) {
    var areNotificationsEnabled by remember { mutableStateOf(true) }

    SettingsContent(
        onBackClick = onBackClick,
        onLogout = onLogout,
        areNotificationsEnabled = areNotificationsEnabled,
        onNotificationsToggle = { newState -> areNotificationsEnabled = newState },
        onManageUsersClick = onManageUsersClick
    )
}

@Composable
fun SettingsContent(
    onBackClick: () -> Unit,
    onLogout: () -> Unit,
    areNotificationsEnabled: Boolean,
    onNotificationsToggle: (Boolean) -> Unit,
    onManageUsersClick: () -> Unit
) {
    Scaffold(
        topBar = {
            ComandaTopAppBar(
                title = stringResource(R.string.settings),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            SettingsGroup(title = stringResource(R.string.appearance)) {
                SettingItem(
                    icon = { Icon(Icons.Default.Palette, stringResource(R.string.theme), tint = MaterialTheme.colorScheme.primary) },
                    title = stringResource(R.string.theme),
                    subtitle = stringResource(R.string.choose_your_preferred_theme),
                    onClick = { /* TODO */ }
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(
                    icon = { Icon(Icons.Default.Language, stringResource(R.string.language), tint = MaterialTheme.colorScheme.primary) },
                    title = stringResource(R.string.language),
                    subtitle = stringResource(R.string.select_your_language),
                    onClick = { /* TODO */ }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            SettingsGroup(title = stringResource(R.string.notifications)) {
                SettingItem(
                    icon = { Icon(Icons.Default.Notifications, stringResource(R.string.notifications), tint = MaterialTheme.colorScheme.primary) },
                    title = stringResource(R.string.enable_notifications),
                    subtitle = stringResource(R.string.receive_alerts_and_updates),
                    onClick = { onNotificationsToggle(!areNotificationsEnabled) },
                    control = { Switch(checked = areNotificationsEnabled, onCheckedChange = onNotificationsToggle) }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            SettingsGroup(title = stringResource(R.string.account)) {
                SettingItem(
                    icon = { Icon(Icons.Default.ManageAccounts, stringResource(R.string.manage_users), tint = MaterialTheme.colorScheme.primary) },
                    title = stringResource(R.string.manage_users),
                    subtitle = stringResource(R.string.manage_users_subtitle),
                    onClick = onManageUsersClick
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(
                    icon = { Icon(Icons.Default.Edit, stringResource(R.string.edit_profile), tint = MaterialTheme.colorScheme.primary) },
                    title = stringResource(R.string.edit_profile),
                    subtitle = stringResource(R.string.edit_profile_subtitle),
                    onClick = { /* TODO */ }
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.Logout, stringResource(R.string.logout), tint = MaterialTheme.colorScheme.primary) },
                    title = stringResource(R.string.logout),
                    subtitle = stringResource(R.string.logout_subtitle),
                    onClick = onLogout
                )
            }
        }
    }
}

@Composable
fun SettingsGroup(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        content()
    }
}

@Composable
fun SettingItem(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    onClick: (() -> Unit)? = null,
    control: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (control != null) {
            Spacer(modifier = Modifier.width(16.dp))
            control()
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SettingsContentPreview() {
    ComandaTheme {
        SettingsContent(
            onBackClick = {},
            onLogout = {},
            areNotificationsEnabled = true,
            onNotificationsToggle = {},
            onManageUsersClick = {}
        )
    }
}
