package dev.giuseppedarro.comanda.features.tables.presentation.components

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.R
import dev.giuseppedarro.comanda.core.ui.theme.ComandaTheme
import org.junit.Rule
import org.junit.Test

class AppDrawerTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

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

        composeTestRule.onNodeWithText(context.getString(R.string.printers)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.menu)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.settings)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.logout)).assertIsDisplayed()
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

        composeTestRule.onNodeWithText(context.getString(R.string.printers)).performClick()

        assertThat(printersClicked).isTrue()
        assertThat(drawerClosed).isTrue()
    }
}