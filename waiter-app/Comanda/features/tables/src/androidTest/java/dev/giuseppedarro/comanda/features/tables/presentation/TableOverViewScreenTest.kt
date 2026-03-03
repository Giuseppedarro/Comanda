package dev.giuseppedarro.comanda.features.tables.presentation

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import dev.giuseppedarro.comanda.features.tables.R
import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.core.ui.theme.ComandaTheme
import org.junit.Rule
import org.junit.Test

class TableOverViewScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

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
                    uiState = TableOverviewUiState(tables = tables),
                    onTableClick = {},
                    onNavigateToPrinters = {},
                    onNavigateToMenu = {},
                    onNavigateToSettings = {},
                    onLogout = {},
                    onRefresh = {},
                    onAddTableClick = {},
                    onFilterChanged = {}
                )
            }
        }

        composeTestRule.onNodeWithText(context.getString(R.string.table_number, 1)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.table_number, 5)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.table_number, 8)).assertIsDisplayed()
    }

    @Test
    fun whenEmptyState_displaysEmptyGrid() {
        composeTestRule.setContent {
            ComandaTheme {
                TableOverviewContent(
                    uiState = TableOverviewUiState(tables = emptyList()),
                    onTableClick = {},
                    onNavigateToPrinters = {},
                    onNavigateToMenu = {},
                    onNavigateToSettings = {},
                    onLogout = {},
                    onRefresh = {},
                    onAddTableClick = {},
                    onFilterChanged = {}
                )
            }
        }

        composeTestRule.onNodeWithText(context.getString(R.string.table_number, 1)).assertDoesNotExist()
    }

    @Test
    fun whenFilterIsApplied_displaysFilteredTables() {
        val tables = listOf(
            Table(number = 1, isOccupied = true),
            Table(number = 2, isOccupied = false),
            Table(number = 3, isOccupied = true)
        )

        composeTestRule.setContent {
            ComandaTheme {
                TableOverviewContent(
                    uiState = TableOverviewUiState(tables = tables, filter = TableFilter.OCCUPIED),
                    onTableClick = {},
                    onNavigateToPrinters = {},
                    onNavigateToMenu = {},
                    onNavigateToSettings = {},
                    onLogout = {},
                    onRefresh = {},
                    onAddTableClick = {},
                    onFilterChanged = {}
                )
            }
        }

        composeTestRule.onNodeWithText(context.getString(R.string.table_number, 1)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.table_number, 3)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.table_number, 2)).assertDoesNotExist()
    }
}