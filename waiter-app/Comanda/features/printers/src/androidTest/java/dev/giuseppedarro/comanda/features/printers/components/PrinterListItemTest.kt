package dev.giuseppedarro.comanda.features.printers.components

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import dev.giuseppedarro.comanda.R
import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.core.ui.theme.ComandaTheme
import dev.giuseppedarro.comanda.features.printers.presentation.components.PrinterListItem
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class PrinterListItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun printerListItem_displaysCorrectly() {
        val printer = Printer(
            id = 1,
            name = "Kitchen Printer 1",
            address = "192.168.1.100",
            port = 9100
        )

        composeTestRule.setContent {
            ComandaTheme {
                PrinterListItem(
                    printer = printer,
                    onEditClick = {},
                    onDeleteClick = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Kitchen Printer 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("192.168.1.100").assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.port_number, 9100)).assertIsDisplayed()
    }

    @Test
    fun whenEditClicked_onEditClickCalled() {
        val printer = Printer(
            id = 1,
            name = "Kitchen Printer",
            address = "192.168.1.100",
            port = 9100
        )
        var clickedPrinter: Printer? = null

        composeTestRule.setContent {
            ComandaTheme {
                PrinterListItem(
                    printer = printer,
                    onEditClick = { clickedPrinter = it },
                    onDeleteClick = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription(context.getString(R.string.edit_printer)).performClick()

        assertEquals(printer, clickedPrinter)
    }

    @Test
    fun whenDeleteClicked_onDeleteClickCalled() {
        val printer = Printer(
            id = 1,
            name = "Kitchen Printer",
            address = "192.168.1.100",
            port = 9100
        )
        var clickedPrinter: Printer? = null

        composeTestRule.setContent {
            ComandaTheme {
                PrinterListItem(
                    printer = printer,
                    onEditClick = {},
                    onDeleteClick = { clickedPrinter = it }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription(context.getString(R.string.delete_printer)).performClick()

        assertEquals(printer, clickedPrinter)
    }

    @Test
    fun whenMultiplePrinters_displaysCorrectly() {
        val printer1 = Printer(
            id = 1,
            name = "Kitchen Printer",
            address = "192.168.1.100",
            port = 9100
        )
        val printer2 = Printer(
            id = 2,
            name = "Bar Printer",
            address = "192.168.1.101",
            port = 9200
        )

        composeTestRule.setContent {
            ComandaTheme {
                Column {
                    PrinterListItem(
                        printer = printer1,
                        onEditClick = {},
                        onDeleteClick = {}
                    )
                    PrinterListItem(
                        printer = printer2,
                        onEditClick = {},
                        onDeleteClick = {}
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("Kitchen Printer").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bar Printer").assertIsDisplayed()
        composeTestRule.onNodeWithText("192.168.1.100").assertIsDisplayed()
        composeTestRule.onNodeWithText("192.168.1.101").assertIsDisplayed()
    }
}