package dev.giuseppedarro.comanda.features.printers.domain.usecase

import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrintersRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class CreatePrinterUseCaseTest {

    @Test
    fun `should create printer and return it`() = runTest {
        // Given
        val expectedPrinter = Printer(1, "Kitchen", "192.168.1.100", 9100)
        val mockRepository = mockk<PrintersRepository>()
        coEvery { mockRepository.createPrinter("Kitchen", "192.168.1.100", 9100) } returns expectedPrinter

        val useCase = CreatePrinterUseCase(mockRepository)

        // When
        val result = useCase("Kitchen", "192.168.1.100", 9100)

        // Then
        assertEquals(expectedPrinter, result)
    }

    @Test
    fun `should pass correct parameters to repository`() = runTest {
        // Given
        val mockRepository = mockk<PrintersRepository>()
        coEvery { mockRepository.createPrinter(any(), any(), any()) } returns Printer(1, "Test", "127.0.0.1", 9100)

        val useCase = CreatePrinterUseCase(mockRepository)

        // When
        useCase("Test", "127.0.0.1", 9200)

        // Then
        coEvery {
            mockRepository.createPrinter("Test", "127.0.0.1", 9200)
        }
    }
}