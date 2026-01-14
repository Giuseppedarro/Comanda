package dev.giuseppedarro.comanda.features.printers.domain.usecase

import dev.giuseppedarro.comanda.features.printers.domain.repository.PrintersRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue

class DeletePrinterUseCaseTest {

    @Test
    fun `should delete printer and return true when printer exists`() = runTest {
        // Given
        val mockRepository = mockk<PrintersRepository>()
        coEvery { mockRepository.deletePrinter(1) } returns true

        val useCase = DeletePrinterUseCase(mockRepository)

        // When
        val result = useCase(1)

        // Then
        assertTrue(result)
    }

    @Test
    fun `should return false when printer not found`() = runTest {
        // Given
        val mockRepository = mockk<PrintersRepository>()
        coEvery { mockRepository.deletePrinter(999) } returns false

        val useCase = DeletePrinterUseCase(mockRepository)

        // When
        val result = useCase(999)

        // Then
        assertTrue(!result)
    }
}