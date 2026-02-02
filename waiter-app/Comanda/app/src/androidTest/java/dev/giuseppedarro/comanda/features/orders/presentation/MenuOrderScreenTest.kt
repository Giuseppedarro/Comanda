package dev.giuseppedarro.comanda.features.orders.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import dev.giuseppedarro.comanda.features.orders.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.orders.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterial3Api::class)
class MenuOrderScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockSheetState = mockk<SheetState>(relaxed = true)

    @Test
    fun whenOrderItemsExist_displaysInList() {
        val orderItems = listOf(
            OrderItem(MenuItem("1", "Gourmet Burger", 1299), 2),
            OrderItem(MenuItem("2", "Cola", 250), 1)
        )
        val menuCategories = emptyList<MenuCategory>()

        composeTestRule.setContent {
            ComandaTheme {
                MenuOrderContent(
                    tableNumber = 5,
                    numberOfPeople = 4,
                    uiState = MenuOrderUiState(
                        orderItems = orderItems,
                        menuCategories = menuCategories
                    ),
                    onQuantityChange = { _, _ -> },
                    onCategorySelected = {},
                    onMenuItemAdded = {},
                    onDismissSheet = {},
                    onSendClick = {},
                    onPrintBillClick = {},
                    sheetState = mockSheetState
                )
            }
        }

        composeTestRule.onNodeWithText("Gourmet Burger").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cola").assertIsDisplayed()
    }

    @Test
    fun whenCategoriesExist_displaysInGrid() {
        val orderItems = emptyList<OrderItem>()
        val menuCategories = listOf(
            MenuCategory("Appetizers", emptyList()),
            MenuCategory("Main Courses", emptyList()),
            MenuCategory("Desserts", emptyList()),
            MenuCategory("Drinks", emptyList())
        )

        composeTestRule.setContent {
            ComandaTheme {
                MenuOrderContent(
                    tableNumber = 3,
                    numberOfPeople = 2,
                    uiState = MenuOrderUiState(
                        orderItems = orderItems,
                        menuCategories = menuCategories
                    ),
                    onQuantityChange = { _, _ -> },
                    onCategorySelected = {},
                    onMenuItemAdded = {},
                    onDismissSheet = {},
                    onSendClick = {},
                    onPrintBillClick = {},
                    sheetState = mockSheetState
                )
            }
        }

        composeTestRule.onNodeWithText("Appetizers").assertIsDisplayed()
        composeTestRule.onNodeWithText("Main Courses").assertIsDisplayed()
        composeTestRule.onNodeWithText("Desserts").assertIsDisplayed()
        composeTestRule.onNodeWithText("Drinks").assertIsDisplayed()
    }

    @Test
    fun whenEmpty_displaysEmptyState() {
        val uiState = MenuOrderUiState(
            orderItems = emptyList(),
            menuCategories = emptyList(),
            isSheetVisible = false
        )

        composeTestRule.setContent {
            ComandaTheme {
                MenuOrderContent(
                    tableNumber = 1,
                    numberOfPeople = 0,
                    uiState = uiState,
                    onQuantityChange = { _, _ -> },
                    onCategorySelected = {},
                    onMenuItemAdded = {},
                    onDismissSheet = {},
                    onSendClick = {},
                    onPrintBillClick = {},
                    sheetState = mockSheetState
                )
            }
        }

        composeTestRule.onNodeWithText("Table 1 - 0 People").assertIsDisplayed()
    }

    @Test
    fun whenMultipleOrderItems_displaysInCorrectOrder() {
        val orderItems = listOf(
            OrderItem(MenuItem("1", "Starter", 500), 1),
            OrderItem(MenuItem("2", "Main Dish", 1500), 2),
            OrderItem(MenuItem("3", "Dessert", 700), 1)
        )
        val menuCategories = emptyList<MenuCategory>()

        composeTestRule.setContent {
            ComandaTheme {
                MenuOrderContent(
                    tableNumber = 8,
                    numberOfPeople = 6,
                    uiState = MenuOrderUiState(
                        orderItems = orderItems,
                        menuCategories = menuCategories
                    ),
                    onQuantityChange = { _, _ -> },
                    onCategorySelected = {},
                    onMenuItemAdded = {},
                    onDismissSheet = {},
                    onSendClick = {},
                    onPrintBillClick = {},
                    sheetState = mockSheetState
                )
            }
        }

        composeTestRule.onNodeWithText("Starter").assertIsDisplayed()
        composeTestRule.onNodeWithText("Main Dish").assertIsDisplayed()
        composeTestRule.onNodeWithText("Dessert").assertIsDisplayed()
    }
}