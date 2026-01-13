package dev.giuseppedarro.comanda.features.printers.domain.usecase

import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrintersRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse

class SendTicketToPrinterUseCaseTest {

    @Test
    fun `should send ticket successfully when printer exists`() = runTest {
        // Given
        val printer = Printer(1, "Kitchen", "192.168.1.100", 9100)
        val mockRepository = mockk<PrintersRepository>()
        coEvery { mockRepository.getPrinterById(1) } returns printer

        val useCase = SendTicketToPrinterUseCase(mockRepository)

        // When
        val result = useCase(1, "Table 5\nPizza x2\nCola x1")

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `should return failure when printer not found`() = runTest {
        // Given
        val mockRepository = mockk<PrintersRepository>()
        coEvery { mockRepository.getPrinterById(999) } returns null

        val useCase = SendTicketToPrinterUseCase(mockRepository)

        // When
        val result = useCase(999, "Table 5\nPizza x2")

        // Then
        assertTrue(result.isFailure)
        assertFalse(result.isSuccess)
    }

    @Test
    fun `should pass correct ticket content to printer`() = runTest {
        // Given
        val ticketContent = """
            ==================
            Table: 5
            Pizza x2
            Cola x1
            ==================
        """.trimIndent()

        val printer = Printer(1, "Kitchen", "192.168.1.100", 9100)
        val mockRepository = mockk<PrintersRepository>()
        coEvery { mockRepository.getPrinterById(1) } returns printer

        val useCase = SendTicketToPrinterUseCase(mockRepository)

        // When
        val result = useCase(1, ticketContent)

        // Then
        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
    }
}