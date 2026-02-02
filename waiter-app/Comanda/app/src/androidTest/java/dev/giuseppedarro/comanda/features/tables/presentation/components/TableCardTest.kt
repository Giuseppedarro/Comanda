package dev.giuseppedarro.comanda.features.tables.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class TableCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun tableCard_whenVacant_displaysCorrectly() {
        composeTestRule.setContent {
            ComandaTheme {
                TableCard(tableNumber = 1, isOccupied = false, onButtonClick = {})
            }
        }

        composeTestRule.onNodeWithText("Table 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Available").assertIsDisplayed()
        composeTestRule.onNodeWithText("Take Order").assertIsDisplayed()
    }

    @Test
    fun tableCard_whenOccupied_displaysCorrectly() {
        composeTestRule.setContent {
            ComandaTheme {
                TableCard(tableNumber = 5, isOccupied = true, onButtonClick = {})
            }
        }

        composeTestRule.onNodeWithText("Table 5").assertIsDisplayed()
        composeTestRule.onNodeWithText("Occupied").assertIsDisplayed()
        composeTestRule.onNodeWithText("Modify Order").assertIsDisplayed()
    }

    @Test
    fun whenButtonClicked_onButtonClickIsCalled() {
        var wasClicked = false

        composeTestRule.setContent {
            ComandaTheme {
                TableCard(tableNumber = 1, isOccupied = false, onButtonClick = { wasClicked = true })
            }
        }

        composeTestRule.onNodeWithText("Take Order").performClick()

        assertTrue(wasClicked)
    }
}