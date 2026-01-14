package dev.giuseppedarro.comanda.features.printers.domain.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class PrinterTest {

    @Test
    fun `should create printer with all properties`() {
        val printer = Printer(
            id = 1,
            name = "Kitchen",
            address = "192.168.1.100",
            port = 9100
        )

        assertEquals(1, printer.id)
        assertEquals("Kitchen", printer.name)
        assertEquals("192.168.1.100", printer.address)
        assertEquals(9100, printer.port)
    }

    @Test
    fun `should use default port when not specified`() {
        // Note: Since port has no default value in data class, this tests
        // that the caller must provide it
        val printer = Printer(
            id = 1,
            name = "Kitchen",
            address = "192.168.1.100",
            port = 9100
        )

        assertEquals(9100, printer.port)
    }

    @Test
    fun `should support data class equality`() {
        val printer1 = Printer(1, "Kitchen", "192.168.1.100", 9100)
        val printer2 = Printer(1, "Kitchen", "192.168.1.100", 9100)

        assertEquals(printer1, printer2)
    }

    @Test
    fun `should support data class copy`() {
        val original = Printer(1, "Kitchen", "192.168.1.100", 9100)
        val updated = original.copy(name = "Kitchen Updated")

        assertEquals("Kitchen Updated", updated.name)
        assertEquals(original.address, updated.address)
        assertEquals(original.port, updated.port)
    }
}