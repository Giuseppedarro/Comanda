package dev.giuseppedarro.comanda.features.settings.presentation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.core.ui.theme.ComandaTheme
import dev.giuseppedarro.comanda.features.settings.R
import org.junit.Rule
import org.junit.Test

class ThemeSettingsScreenInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun darkModeControlDisabledWhenSystemThemeIsEnabled() {
        composeRule.setContent {
            ComandaTheme {
                ThemeSettingsContent(
                    onBackClick = {},
                    useSystemTheme = true,
                    onUseSystemThemeChange = {},
                    isDarkMode = false,
                    onDarkModeChange = {}
                )
            }
        }

        val darkMode = composeRule.activity.getString(R.string.dark_mode)
        composeRule.onNodeWithText(darkMode).assertIsDisplayed()
        composeRule.onNodeWithTag("switch_dark_mode").assertIsNotEnabled()
    }

    @Test
    fun clickingUseSystemThemeItemInvokesCallback() {
        var newValue: Boolean? = null

        composeRule.setContent {
            ComandaTheme {
                ThemeSettingsContent(
                    onBackClick = {},
                    useSystemTheme = false,
                    onUseSystemThemeChange = { newValue = it },
                    isDarkMode = false,
                    onDarkModeChange = {}
                )
            }
        }

        val title = composeRule.activity.getString(R.string.use_system_theme)
        composeRule.onAllNodesWithText(title)[0].performClick()

        assertThat(newValue).isEqualTo(true)
    }

    @Test
    fun darkModeControlEnabledWhenSystemThemeIsDisabled() {
        composeRule.setContent {
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

        composeRule.onNodeWithTag("switch_dark_mode").assertIsEnabled()
    }
}
