package dev.giuseppedarro.comanda.features.printers.domain.use_case

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrinterRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdatePrinterUseCaseTest {

    private lateinit var updatePrinterUseCase: UpdatePrinterUseCase
    private lateinit var printerRepository: PrinterRepository

    @Before
    fun setUp() {
        printerRepository = mockk()
        updatePrinterUseCase = UpdatePrinterUseCase(printerRepository)
    }

    @Test
    fun `invoke should call updatePrinter on repository`() = runTest {
        // Given
        val printer = Printer(1, "Kitchen Printer", "192.168.1.100", 9100)
        coEvery { printerRepository.updatePrinter(any(), any(), any(), any()) } returns Result.success(printer)

        // When
        val result = updatePrinterUseCase(1, "Kitchen Printer", "192.168.1.100", 9100)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(printer)
    }
}