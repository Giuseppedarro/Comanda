package dev.giuseppedarro.comanda.features.tables.presentation.components

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import dev.giuseppedarro.comanda.R
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class TableCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun tableCard_whenVacant_displaysCorrectly() {
        composeTestRule.setContent {
            ComandaTheme {
                TableCard(tableNumber = 1, isOccupied = false, onButtonClick = {})
            }
        }

        composeTestRule.onNodeWithText(context.getString(R.string.table_number, 1)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.available)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.take_order)).assertIsDisplayed()
    }

    @Test
    fun tableCard_whenOccupied_displaysCorrectly() {
        composeTestRule.setContent {
            ComandaTheme {
                TableCard(tableNumber = 5, isOccupied = true, onButtonClick = {})
            }
        }

        composeTestRule.onNodeWithText(context.getString(R.string.table_number, 5)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.occupied)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.modify_order)).assertIsDisplayed()
    }

    @Test
    fun whenButtonClicked_onButtonClickIsCalled() {
        var wasClicked = false

        composeTestRule.setContent {
            ComandaTheme {
                TableCard(tableNumber = 1, isOccupied = false, onButtonClick = { wasClicked = true })
            }
        }

        composeTestRule.onNodeWithText(context.getString(R.string.take_order)).performClick()

        assertTrue(wasClicked)
    }
}