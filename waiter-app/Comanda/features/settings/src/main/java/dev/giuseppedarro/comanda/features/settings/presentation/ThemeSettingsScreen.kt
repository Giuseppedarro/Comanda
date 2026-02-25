
package dev.giuseppedarro.comanda.features.settings.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Tonality
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.core.presentation.ComandaTopAppBar
import dev.giuseppedarro.comanda.core.ui.theme.ComandaTheme
import dev.giuseppedarro.comanda.features.settings.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun ThemeSettingsScreen(
    onBackClick: () -> Unit,
    viewModel: ThemeSettingsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ThemeSettingsContent(
        onBackClick = onBackClick,
        useSystemTheme = uiState.useSystemTheme,
        onUseSystemThemeChange = viewModel::onUseSystemThemeChange,
        isDarkMode = uiState.isDarkMode,
        onDarkModeChange = viewModel::onDarkModeChange
    )
}

@Composable
fun ThemeSettingsContent(
    onBackClick: () -> Unit,
    useSystemTheme: Boolean,
    onUseSystemThemeChange: (Boolean) -> Unit,
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            ComandaTopAppBar(
                title = stringResource(R.string.theme),
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
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            SettingsGroup(title = stringResource(R.string.theme_options)) {
                SettingItem(
                    icon = { Icon(Icons.Default.Tonality, stringResource(R.string.use_system_theme), tint = MaterialTheme.colorScheme.primary) },
                    title = stringResource(R.string.use_system_theme),
                    subtitle = stringResource(R.string.follow_system_theme),
                    onClick = { onUseSystemThemeChange(!useSystemTheme) },
                    control = { Switch(checked = useSystemTheme, onCheckedChange = onUseSystemThemeChange) }
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingItem(
                    icon = { Icon(Icons.Default.DarkMode, stringResource(R.string.dark_mode), tint = MaterialTheme.colorScheme.primary) },
                    title = stringResource(R.string.dark_mode),
                    subtitle = stringResource(R.string.enable_dark_mode),
                    onClick = { onDarkModeChange(!isDarkMode) },
                    control = { Switch(checked = isDarkMode, onCheckedChange = onDarkModeChange, enabled = !useSystemTheme) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ThemeSettingsContentPreview() {
    ComandaTheme {
        ThemeSettingsContent(
            onBackClick = {},
            useSystemTheme = false,
            onUseSystemThemeChange = {},
            isDarkMode = false,
            onDarkModeChange = {}
        )
    }
}
