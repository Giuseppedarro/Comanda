package dev.giuseppedarro.comanda.features.orders.presentation.components

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import dev.giuseppedarro.comanda.R
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MenuItemRowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun menuItemRow_displaysCorrectly() {
        composeTestRule.setContent {
            ComandaTheme {
                MenuItemRow(
                    itemName = "Gourmet Burger",
                    itemPrice = "€12.99",
                    quantity = 2,
                    onQuantityChange = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Gourmet Burger").assertIsDisplayed()
        composeTestRule.onNodeWithText("€12.99").assertIsDisplayed()
        composeTestRule.onNodeWithText("2").assertIsDisplayed()
    }

    @Test
    fun whenAddClicked_onQuantityChangeIsCalled() {
        var newQuantity: Int? = null

        composeTestRule.setContent {
            ComandaTheme {
                MenuItemRow(
                    itemName = "Gourmet Burger",
                    itemPrice = "€12.99",
                    quantity = 1,
                    onQuantityChange = { quantity ->
                        newQuantity = quantity
                    }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription(context.getString(R.string.add)).performClick()

        assertEquals(2, newQuantity)
    }

    @Test
    fun whenRemoveClicked_onQuantityChangeIsCalled() {
        var newQuantity: Int? = null

        composeTestRule.setContent {
            ComandaTheme {
                MenuItemRow(
                    itemName = "Gourmet Burger",
                    itemPrice = "€12.99",
                    quantity = 5,
                    onQuantityChange = { quantity ->
                        newQuantity = quantity
                    }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription(context.getString(R.string.remove)).performClick()

        assertEquals(4, newQuantity)
    }
}
