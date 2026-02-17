package dev.giuseppedarro.comanda.features.printers.domain.use_case

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrinterRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeletePrinterUseCaseTest {

    private lateinit var deletePrinterUseCase: DeletePrinterUseCase
    private lateinit var printerRepository: PrinterRepository

    @Before
    fun setUp() {
        printerRepository = mockk()
        deletePrinterUseCase = DeletePrinterUseCase(printerRepository)
    }

    @Test
    fun `invoke should call deletePrinter on repository`() = runTest {
        // Given
        coEvery { printerRepository.deletePrinter(any()) } returns Result.success(Unit)

        // When
        val result = deletePrinterUseCase(1)

        // Then
        assertThat(result.isSuccess).isTrue()
    }
}