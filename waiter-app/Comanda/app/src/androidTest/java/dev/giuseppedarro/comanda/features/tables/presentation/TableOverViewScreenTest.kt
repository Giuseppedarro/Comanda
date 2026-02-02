package dev.giuseppedarro.comanda.features.tables.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme
import org.junit.Rule
import org.junit.Test

class TableOverViewScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenTablesAreLoaded_displaysTablesInGrid() {
        val tables = listOf(
            Table(number = 1, isOccupied = false),
            Table(number = 5, isOccupied = true),
            Table(number = 8, isOccupied = false)
        )

        composeTestRule.setContent {
            ComandaTheme {
                TableOverviewContent(
                    tables = tables,
                    onTableClick = {},
                    onNavigateToPrinters = {},
                    onNavigateToMenu = {},
                    onNavigateToSettings = {},
                    onLogout = {},
                    isRefreshing = false,
                    onRefresh = {},
                    onAddTableClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Table 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Table 5").assertIsDisplayed()
        composeTestRule.onNodeWithText("Table 8").assertIsDisplayed()
    }

    @Test
    fun whenEmptyState_displaysEmptyGrid() {
        composeTestRule.setContent {
            ComandaTheme {
                TableOverviewContent(
                    tables = emptyList(),
                    onTableClick = {},
                    onNavigateToPrinters = {},
                    onNavigateToMenu = {},
                    onNavigateToSettings = {},
                    onLogout = {},
                    isRefreshing = false,
                    onRefresh = {},
                    onAddTableClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Table 1").assertDoesNotExist()
    }

    @Test
    fun whenMultipleTables_displaysInCorrectOrder() {
        val tables = listOf(
            Table(number = 10, isOccupied = true),
            Table(number = 2, isOccupied = false),
            Table(number = 15, isOccupied = true)
        )

        composeTestRule.setContent {
            ComandaTheme {
                TableOverviewContent(
                    tables = tables,
                    onTableClick = {},
                    onNavigateToPrinters = {},
                    onNavigateToMenu = {},
                    onNavigateToSettings = {},
                    onLogout = {},
                    isRefreshing = false,
                    onRefresh = {},
                    onAddTableClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Table 10").assertIsDisplayed()
        composeTestRule.onNodeWithText("Table 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("Table 15").assertIsDisplayed()
    }
}