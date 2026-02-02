package dev.giuseppedarro.comanda.features.tables.presentation.components

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class TableDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun tableDialog_displaysCorrectly() {
        composeTestRule.setContent {
            ComandaTheme {
                TableDialog(
                    onDismissRequest = {},
                    onConfirmClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Open Table").assertIsDisplayed()
        composeTestRule.onNodeWithText("How many people are sitting at this table?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Number of people").assertIsDisplayed()
    }

    @Test
    fun whenEmptyInput_confirmButtonDisabled() {
        composeTestRule.setContent {
            ComandaTheme {
                TableDialog(
                    onDismissRequest = {},
                    onConfirmClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Confirm").assertIsNotEnabled()
    }

    @Test
    fun whenConfirmClicked_onConfirmClickCalledWithValue() {
        var clickedValue: Int? = null

        composeTestRule.setContent {
            ComandaTheme {
                TableDialog(
                    onDismissRequest = {},
                    onConfirmClick = { value -> clickedValue = value }
                )
            }
        }

        composeTestRule.onNodeWithText("Number of people")
            .performTextInput("3")

        composeTestRule.onNodeWithText("Confirm").performClick()

        assertEquals(3, clickedValue)
    }

    @Test
    fun whenCancelClicked_onDismissRequestCalled() {
        var dismissCalled = false

        composeTestRule.setContent {
            ComandaTheme {
                TableDialog(
                    onDismissRequest = { dismissCalled = true },
                    onConfirmClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Cancel").performClick()

        assertTrue(dismissCalled)
    }

    @Test
    fun whenInputChangesToLetter_rejected() {
        composeTestRule.setContent {
            ComandaTheme {
                TableDialog(
                    onDismissRequest = {},
                    onConfirmClick = {}
                )
            }
        }

        val textNode = composeTestRule.onNodeWithText("Number of people")

        textNode.performTextInput("5")
        textNode.performTextClearance() // For clearing

        textNode.performTextInput("a")

        composeTestRule.onNodeWithText("Confirm").assertIsNotEnabled()
    }
}