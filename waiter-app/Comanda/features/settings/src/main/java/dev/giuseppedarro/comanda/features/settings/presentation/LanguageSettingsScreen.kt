package dev.giuseppedarro.comanda.features.settings.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.core.presentation.ComandaTopAppBar
import dev.giuseppedarro.comanda.core.ui.theme.ComandaTheme
import dev.giuseppedarro.comanda.core.R
import dev.giuseppedarro.comanda.features.settings.R as SettingsR
import org.koin.androidx.compose.koinViewModel

@Composable
fun LanguageSettingsScreen(
    onBackClick: () -> Unit,
    viewModel: LanguageSettingsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LanguageSettingsContent(
        onBackClick = onBackClick,
        currentLanguage = uiState.currentLanguage,
        onLanguageChange = viewModel::onLanguageChange
    )
}

@Composable
fun LanguageSettingsContent(
    onBackClick: () -> Unit,
    currentLanguage: String,
    onLanguageChange: (String) -> Unit
) {
    Scaffold(
        topBar = {
            ComandaTopAppBar(
                title = stringResource(SettingsR.string.language),
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
            SettingsGroup(title = stringResource(SettingsR.string.select_your_language)) {
                SettingItem(
                    icon = { RadioButton(selected = currentLanguage == "en", onClick = { onLanguageChange("en") }) },
                    title = stringResource(SettingsR.string.language_english),
                    subtitle = "English",
                    onClick = { onLanguageChange("en") }
                )
                Divider()
                SettingItem(
                    icon = { RadioButton(selected = currentLanguage == "it", onClick = { onLanguageChange("it") }) },
                    title = stringResource(SettingsR.string.language_italian),
                    subtitle = "Italiano",
                    onClick = { onLanguageChange("it") }
                )
                Divider()
                SettingItem(
                    icon = { RadioButton(selected = currentLanguage == "nl", onClick = { onLanguageChange("nl") }) },
                    title = stringResource(SettingsR.string.language_dutch),
                    subtitle = "Nederlands",
                    onClick = { onLanguageChange("nl") }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LanguageSettingsContentPreview() {
    ComandaTheme {
        LanguageSettingsContent(
            onBackClick = {},
            currentLanguage = "en",
            onLanguageChange = {}
        )
    }
}
