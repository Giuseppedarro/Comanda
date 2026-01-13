package dev.giuseppedarro.comanda.features.printers.domain.usecase

import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrintersRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class GetAllPrintersUseCaseTest {

    @Test
    fun `should return empty list when repository returns empty`() = runTest {
        // Given
        val mockRepository = mockk<PrintersRepository>()
        coEvery { mockRepository.getAllPrinters() } returns emptyList()

        val useCase = GetAllPrintersUseCase(mockRepository)

        // When
        val result = useCase()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `should return all printers from repository`() = runTest {
        // Given
        val expectedPrinters = listOf(
            Printer(1, "Kitchen", "192.168.1.100", 9100),
            Printer(2, "Bar", "192.168.1.101", 9100)
        )
        val mockRepository = mockk<PrintersRepository>()
        coEvery { mockRepository.getAllPrinters() } returns expectedPrinters

        val useCase = GetAllPrintersUseCase(mockRepository)

        // When
        val result = useCase()

        // Then
        assertEquals(2, result.size)
        assertEquals(expectedPrinters, result)
    }
}