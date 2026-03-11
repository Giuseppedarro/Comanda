package dev.giuseppedarro.comanda.features.settings.presentation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.core.ui.theme.ComandaTheme
import dev.giuseppedarro.comanda.features.settings.R
import org.junit.Rule
import org.junit.Test

class SettingsScreenInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun settingsActionsInvokeExpectedCallbacks() {
        var logoutClicked = false
        var manageUsersClicked = false
        var themeClicked = false
        var languageClicked = false

        composeRule.setContent {
            ComandaTheme {
                SettingsContent(
                    onBackClick = {},
                    onLogout = { logoutClicked = true },
                    areNotificationsEnabled = true,
                    onNotificationsToggle = {},
                    onManageUsersClick = { manageUsersClicked = true },
                    onThemeSettingsClick = { themeClicked = true },
                    onLanguageSettingsClick = { languageClicked = true }
                )
            }
        }

        val theme = composeRule.activity.getString(R.string.theme)
        val language = composeRule.activity.getString(R.string.language)
        val manageUsers = composeRule.activity.getString(R.string.manage_users)
        val logout = composeRule.activity.getString(R.string.logout)

        composeRule.onAllNodesWithText(theme)[0].performClick()
        composeRule.onAllNodesWithText(language)[0].performClick()
        composeRule.onAllNodesWithText(manageUsers)[0].performClick()
        composeRule.onAllNodesWithText(logout)[0].performClick()

        assertThat(themeClicked).isTrue()
        assertThat(languageClicked).isTrue()
        assertThat(manageUsersClicked).isTrue()
        assertThat(logoutClicked).isTrue()
    }

    @Test
    fun notificationToggleInvokesCallbackWithNewValue() {
        var toggledValue: Boolean? = null

        composeRule.setContent {
            ComandaTheme {
                SettingsContent(
                    onBackClick = {},
                    onLogout = {},
                    areNotificationsEnabled = true,
                    onNotificationsToggle = { toggledValue = it },
                    onManageUsersClick = {},
                    onThemeSettingsClick = {},
                    onLanguageSettingsClick = {}
                )
            }
        }

        val enableNotifications = composeRule.activity.getString(R.string.enable_notifications)
        composeRule.onAllNodesWithText(enableNotifications)[0].performClick()

        assertThat(toggledValue).isEqualTo(false)
    }
}
