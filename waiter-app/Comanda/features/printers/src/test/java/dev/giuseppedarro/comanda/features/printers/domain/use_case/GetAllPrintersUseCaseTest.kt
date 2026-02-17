package dev.giuseppedarro.comanda.features.printers.domain.use_case

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrinterRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetAllPrintersUseCaseTest {

    private lateinit var getAllPrintersUseCase: GetAllPrintersUseCase
    private lateinit var printerRepository: PrinterRepository

    @Before
    fun setUp() {
        printerRepository = mockk()
        getAllPrintersUseCase = GetAllPrintersUseCase(printerRepository)
    }

    @Test
    fun `invoke should return printers from repository`() = runTest {
        // Given
        val printers = listOf(Printer(1, "Kitchen Printer", "192.168.1.100", 9100))
        coEvery { printerRepository.getAllPrinters() } returns Result.success(printers)

        // When
        val result = getAllPrintersUseCase()

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(printers)
    }
}