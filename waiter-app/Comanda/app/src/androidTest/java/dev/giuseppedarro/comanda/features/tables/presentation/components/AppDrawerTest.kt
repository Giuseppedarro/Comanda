package dev.giuseppedarro.comanda.features.tables.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme
import org.junit.Rule
import org.junit.Test

class AppDrawerTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenDrawerIsOpened_displaysAllMenuItems() {
        composeTestRule.setContent {
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

        composeTestRule.onNodeWithText("Printers").assertIsDisplayed()
        composeTestRule.onNodeWithText("Menu").assertIsDisplayed()
        composeTestRule.onNodeWithText("Settings (to be implemented)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Logout").assertIsDisplayed()
    }

    @Test
    fun whenPrinterItemIsClicked_callsNavigateToPrinters() {
        var printersClicked = false
        val onNavigateToPrinters = { printersClicked = true }

        var drawerClosed = false
        val onCloseDrawer = { drawerClosed = true }

        composeTestRule.setContent {
            ComandaTheme {
                AppDrawer(
                    onNavigateToPrinters = onNavigateToPrinters,
                    onNavigateToMenu = {},
                    onNavigateToSettings = {},
                    onLogout = {},
                    onCloseDrawer = onCloseDrawer
                )
            }
        }

        composeTestRule.onNodeWithText("Printers").performClick()

        assertThat(printersClicked).isTrue()
        assertThat(drawerClosed).isTrue()
    }
}