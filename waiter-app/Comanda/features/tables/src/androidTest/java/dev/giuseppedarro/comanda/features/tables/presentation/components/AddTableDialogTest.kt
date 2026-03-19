package dev.giuseppedarro.comanda.features.tables.presentation.components

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import dev.giuseppedarro.comanda.features.tables.R
import dev.giuseppedarro.comanda.core.ui.theme.ComandaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AddTableDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun whenDialogIsShown_displaysOptions() {
        composeTestRule.setContent {
            ComandaTheme {
                AddTableDialog(
                    onDismissRequest = {},
                    onConfirmClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText(context.getString(R.string.add_new_table)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.next_available)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.specific_number)).assertIsDisplayed()
    }

    @Test
    fun whenIncrementalSelected_confirmIsAlwaysEnabledAndReturnsNull() {
        var callCount = 0
        var capturedValue: Int? = -1 // Start with non-null value
        
        composeTestRule.setContent {
            ComandaTheme {
                AddTableDialog(
                    onDismissRequest = {},
                    onConfirmClick = {
                        callCount++
                        capturedValue = it
                    }
                )
            }
        }

        // Incremental (Next Available) is default
        composeTestRule.onNodeWithText(context.getString(R.string.add)).assertIsEnabled()
        composeTestRule.onNodeWithText(context.getString(R.string.add)).performClick()

        assertEquals(1, callCount)
        assertEquals(null, capturedValue)
    }

    @Test
    fun whenCustomSelected_confirmIsDisabledUntilValidNumberEntered() {
        var callCount = 0
        var capturedValue: Int? = null

        composeTestRule.setContent {
            ComandaTheme {
                AddTableDialog(
                    onDismissRequest = {},
                    onConfirmClick = {
                        callCount++
                        capturedValue = it
                    }
                )
            }
        }

        // Select custom (Specific Number)
        composeTestRule.onNodeWithText(context.getString(R.string.specific_number)).performClick()
        
        // Add button should be disabled as text field is empty
        composeTestRule.onNodeWithText(context.getString(R.string.add)).assertIsNotEnabled()

        // Enter number
        composeTestRule.onNodeWithText(context.getString(R.string.table_number_hint)).performTextInput("15")
        
        // Now it should be enabled
        composeTestRule.onNodeWithText(context.getString(R.string.add)).assertIsEnabled()
        composeTestRule.onNodeWithText(context.getString(R.string.add)).performClick()

        assertEquals(1, callCount)
        assertEquals(15, capturedValue)
    }
}
