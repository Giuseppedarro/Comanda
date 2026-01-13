package dev.giuseppedarro.comanda.features.printers.data.repository

import dev.giuseppedarro.comanda.features.printers.data.Printers
import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull

class PrintersRepositoryImplTest {

    private lateinit var repository: PrintersRepositoryImpl

    companion object {
        @BeforeAll
        @JvmStatic
        fun setupDatabase() {
            // Create an in-memory H2 database for testing
            Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver", user = "sa", password = "")
        }
    }

    @BeforeEach
    fun setup() {
        repository = PrintersRepositoryImpl()
        transaction {
            SchemaUtils.create(Printers)
        }
    }

    @AfterEach
    fun cleanup() {
        transaction {
            SchemaUtils.drop(Printers)
        }
    }

    @Test
    fun `getAllPrinters should return empty list when no printers exist`() = runBlocking {
        val result = repository.getAllPrinters()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getAllPrinters should return all printers`() = runBlocking {
        // Given
        repository.createPrinter("Kitchen", "192.168.1.100", 9100)
        repository.createPrinter("Bar", "192.168.1.101", 9100)

        // When
        val result = repository.getAllPrinters()

        // Then
        assertEquals(2, result.size)
        assertEquals("Kitchen", result[0].name)
        assertEquals("Bar", result[1].name)
    }

    @Test
    fun `createPrinter should create a new printer`() = runBlocking {
        // When
        val printer = repository.createPrinter("Kitchen", "192.168.1.100", 9100)

        // Then
        assertNotNull(printer.id)
        assertEquals("Kitchen", printer.name)
        assertEquals("192.168.1.100", printer.address)
        assertEquals(9100, printer.port)
    }

    @Test
    fun `getPrinterById should return printer if exists`() = runBlocking {
        // Given
        val created = repository.createPrinter("Kitchen", "192.168.1.100", 9100)

        // When
        val found = repository.getPrinterById(created.id)

        // Then
        assertNotNull(found)
        assertEquals(created.id, found!!.id)
        assertEquals("Kitchen", found.name)
    }

    @Test
    fun `getPrinterById should return null if not exists`() = runBlocking {
        val found = repository.getPrinterById(999)
        assertNull(found)
    }

    @Test
    fun `updatePrinter should update existing printer`() = runBlocking {
        // Given
        val created = repository.createPrinter("Kitchen", "192.168.1.100", 9100)

        // When
        val updated = repository.updatePrinter(created.id, "Kitchen Updated", "192.168.1.200", 9200)

        // Then
        assertNotNull(updated)
        assertEquals("Kitchen Updated", updated!!.name)
        assertEquals("192.168.1.200", updated.address)
        assertEquals(9200, updated.port)
    }

    @Test
    fun `updatePrinter should return null if printer not exists`() = runBlocking {
        val result = repository.updatePrinter(999, "Test", "127.0.0.1", 9100)
        assertNull(result)
    }

    @Test
    fun `deletePrinter should delete existing printer`() = runBlocking {
        // Given
        val created = repository.createPrinter("Kitchen", "192.168.1.100", 9100)

        // When
        val deleted = repository.deletePrinter(created.id)
        val afterDelete = repository.getPrinterById(created.id)

        // Then
        assertTrue(deleted)
        assertNull(afterDelete)
    }

    @Test
    fun `deletePrinter should return false if printer not exists`() = runBlocking {
        val deleted = repository.deletePrinter(999)
        assertTrue(!deleted)
    }
}