package dev.giuseppedarro.comanda.features.printers.domain.use_case

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrinterRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CreatePrinterUseCaseTest {

    private lateinit var createPrinterUseCase: CreatePrinterUseCase
    private lateinit var printerRepository: PrinterRepository

    @Before
    fun setUp() {
        printerRepository = mockk()
        createPrinterUseCase = CreatePrinterUseCase(printerRepository)
    }

    @Test
    fun `invoke should call createPrinter on repository`() = runTest {
        // Given
        val printer = Printer(1, "Kitchen Printer", "192.168.1.100", 9100)
        coEvery { printerRepository.createPrinter(any(), any(), any()) } returns Result.success(printer)

        // When
        val result = createPrinterUseCase("Kitchen Printer", "192.168.1.100", 9100)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(printer)
    }
}