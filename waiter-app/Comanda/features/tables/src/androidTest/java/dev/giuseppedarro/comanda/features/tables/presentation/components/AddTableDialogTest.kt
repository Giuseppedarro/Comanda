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
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
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
        composeTestRule.onNodeWithText(context.getString(R.string.incremental_add)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.custom_add)).assertIsDisplayed()
    }

    @Test
    fun whenIncrementalSelected_confirmIsAlwaysEnabledAndReturnsNull() {
        val onConfirmClick: (Int?) -> Unit = mockk(relaxed = true)
        
        composeTestRule.setContent {
            ComandaTheme {
                AddTableDialog(
                    onDismissRequest = {},
                    onConfirmClick = onConfirmClick
                )
            }
        }

        // Incremental is default
        composeTestRule.onNodeWithText(context.getString(R.string.add)).assertIsEnabled()
        composeTestRule.onNodeWithText(context.getString(R.string.add)).performClick()

        verify { onConfirmClick(null) }
        confirmVerified(onConfirmClick)
    }

    @Test
    fun whenCustomSelected_confirmIsDisabledUntilValidNumberEntered() {
        val onConfirmClick: (Int?) -> Unit = mockk(relaxed = true)

        composeTestRule.setContent {
            ComandaTheme {
                AddTableDialog(
                    onDismissRequest = {},
                    onConfirmClick = onConfirmClick
                )
            }
        }

        // Select custom
        composeTestRule.onNodeWithText(context.getString(R.string.custom_add)).performClick()
        
        // Add button should be disabled as text field is empty
        composeTestRule.onNodeWithText(context.getString(R.string.add)).assertIsNotEnabled()

        // Enter number
        composeTestRule.onNodeWithText(context.getString(R.string.table_number_hint)).performTextInput("15")
        
        // Now it should be enabled
        composeTestRule.onNodeWithText(context.getString(R.string.add)).assertIsEnabled()
        composeTestRule.onNodeWithText(context.getString(R.string.add)).performClick()

        verify { onConfirmClick(15) }
    }
}
