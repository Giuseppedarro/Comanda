package dev.giuseppedarro.comanda.features.printers.domain.usecase

import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrintersRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull

class UpdatePrinterUseCaseTest {

    @Test
    fun `should update existing printer`() = runTest {
        // Given
        val updatedPrinter = Printer(1, "Kitchen Updated", "192.168.1.200", 9200)
        val mockRepository = mockk<PrintersRepository>()
        coEvery { mockRepository.updatePrinter(1, "Kitchen Updated", "192.168.1.200", 9200) } returns updatedPrinter

        val useCase = UpdatePrinterUseCase(mockRepository)

        // When
        val result = useCase(1, "Kitchen Updated", "192.168.1.200", 9200)

        // Then
        assertEquals(updatedPrinter, result)
    }

    @Test
    fun `should return null when printer not found`() = runTest {
        // Given
        val mockRepository = mockk<PrintersRepository>()
        coEvery { mockRepository.updatePrinter(999, any(), any(), any()) } returns null

        val useCase = UpdatePrinterUseCase(mockRepository)

        // When
        val result = useCase(999, "Test", "127.0.0.1", 9100)

        // Then
        assertNull(result)
    }
}