package dev.giuseppedarro.comanda.features.settings.presentation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.core.ui.theme.ComandaTheme
import dev.giuseppedarro.comanda.features.settings.R
import org.junit.Rule
import org.junit.Test

class LanguageSettingsScreenInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun languageOptionsDisabledWhenUsingSystemLanguage() {
        composeRule.setContent {
            ComandaTheme {
                LanguageSettingsContent(
                    onBackClick = {},
                    currentLanguage = "en",
                    useSystemLanguage = true,
                    onLanguageChange = {},
                    onUseSystemLanguageChange = {}
                )
            }
        }

        composeRule.onNodeWithTag("radio_en").assertIsNotEnabled()
        composeRule.onNodeWithTag("radio_it").assertIsNotEnabled()
        composeRule.onNodeWithTag("radio_nl").assertIsNotEnabled()
    }

    @Test
    fun languageOptionsEnabledWhenSystemLanguageDisabled() {
        composeRule.setContent {
            ComandaTheme {
                LanguageSettingsContent(
                    onBackClick = {},
                    currentLanguage = "en",
                    useSystemLanguage = false,
                    onLanguageChange = {},
                    onUseSystemLanguageChange = {}
                )
            }
        }

        composeRule.onNodeWithTag("radio_en").assertIsEnabled()
        composeRule.onNodeWithTag("radio_it").assertIsEnabled()
        composeRule.onNodeWithTag("radio_nl").assertIsEnabled()
    }

    @Test
    fun clickingItalianLanguageInvokesCallback() {
        var selectedLanguage: String? = null

        composeRule.setContent {
            ComandaTheme {
                LanguageSettingsContent(
                    onBackClick = {},
                    currentLanguage = "en",
                    useSystemLanguage = false,
                    onLanguageChange = { selectedLanguage = it },
                    onUseSystemLanguageChange = {}
                )
            }
        }

        val italian = composeRule.activity.getString(R.string.language_italian)
        composeRule.onAllNodesWithText(italian)[0].performClick()

        assertThat(selectedLanguage).isEqualTo("it")
    }
}
