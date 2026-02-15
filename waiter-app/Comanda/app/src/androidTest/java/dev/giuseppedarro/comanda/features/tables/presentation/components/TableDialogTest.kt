package dev.giuseppedarro.comanda.features.tables.presentation.components

import android.content.Context
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import dev.giuseppedarro.comanda.R
import dev.giuseppedarro.comanda.core.ui.theme.ComandaTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class TableDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

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

        composeTestRule.onNodeWithText(context.getString(R.string.open_table)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.how_many_people_are_sitting_at_this_table)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.number_of_people)).assertIsDisplayed()
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

        composeTestRule.onNodeWithText(context.getString(R.string.confirm)).assertIsNotEnabled()
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

        composeTestRule.onNodeWithText(context.getString(R.string.number_of_people))
            .performTextInput("3")

        composeTestRule.onNodeWithText(context.getString(R.string.confirm)).performClick()

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

        composeTestRule.onNodeWithText(context.getString(R.string.cancel)).performClick()

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

        val textNode = composeTestRule.onNodeWithText(context.getString(R.string.number_of_people))

        textNode.performTextInput("5")
        textNode.performTextClearance() // For clearing

        textNode.performTextInput("a")

        composeTestRule.onNodeWithText(context.getString(R.string.confirm)).assertIsNotEnabled()
    }
}