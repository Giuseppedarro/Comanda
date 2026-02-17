package dev.giuseppedarro.comanda.features.printers.data.repository

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.printers.data.remote.PrinterApi
import dev.giuseppedarro.comanda.features.printers.data.remote.dto.PrinterDto
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrinterRepository
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PrinterRepositoryImplTest {

    private lateinit var printerRepository: PrinterRepository
    private lateinit var printerApi: PrinterApi

    @Before
    fun setUp() {
        printerApi = mockk()
        printerRepository = PrinterRepositoryImpl(printerApi)
    }

    @Test
    fun `getAllPrinters should return success when api is successful`() = runTest {
        // Given
        val printerDtos = listOf(PrinterDto(1, "Kitchen Printer", "192.168.1.100", 9100))
        coEvery { printerApi.getAllPrinters() } returns printerDtos

        // When
        val result = printerRepository.getAllPrinters()

        // Then
        assertThat(result.isSuccess).isTrue()
        val printers = result.getOrNull()
        assertThat(printers).isNotNull()
        assertThat(printers!!).hasSize(1)
        assertThat(printers[0].id).isEqualTo(1)
        assertThat(printers[0].name).isEqualTo("Kitchen Printer")
    }

    @Test
    fun `getAllPrinters should return failure when api throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Error")
        coEvery { printerApi.getAllPrinters() } throws exception

        // When
        val result = printerRepository.getAllPrinters()

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `createPrinter should return success when api is successful`() = runTest {
        // Given
        val printerDto = PrinterDto(1, "Kitchen Printer", "192.168.1.100", 9100)
        coEvery { printerApi.createPrinter(any()) } returns printerDto

        // When
        val result = printerRepository.createPrinter("Kitchen Printer", "192.168.1.100", 9100)

        // Then
        assertThat(result.isSuccess).isTrue()
        val printer = result.getOrNull()
        assertThat(printer).isNotNull()
        assertThat(printer!!.id).isEqualTo(1)
        assertThat(printer.name).isEqualTo("Kitchen Printer")
    }

    @Test
    fun `createPrinter should return failure when api throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Error")
        coEvery { printerApi.createPrinter(any()) } throws exception

        // When
        val result = printerRepository.createPrinter("Kitchen Printer", "192.168.1.100", 9100)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `updatePrinter should return success when api is successful`() = runTest {
        // Given
        val printerDto = PrinterDto(1, "Kitchen Printer", "192.168.1.100", 9100)
        coEvery { printerApi.updatePrinter(any(), any()) } returns printerDto

        // When
        val result = printerRepository.updatePrinter(1, "Kitchen Printer", "192.168.1.100", 9100)

        // Then
        assertThat(result.isSuccess).isTrue()
        val printer = result.getOrNull()
        assertThat(printer).isNotNull()
        assertThat(printer!!.id).isEqualTo(1)
        assertThat(printer.name).isEqualTo("Kitchen Printer")
    }

    @Test
    fun `updatePrinter should return failure when api throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Error")
        coEvery { printerApi.updatePrinter(any(), any()) } throws exception

        // When
        val result = printerRepository.updatePrinter(1, "Kitchen Printer", "192.168.1.100", 9100)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `deletePrinter should return success when api is successful`() = runTest {
        // Given
        coJustRun { printerApi.deletePrinter(any()) }

        // When
        val result = printerRepository.deletePrinter(1)

        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `deletePrinter should return failure when api throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Error")
        coEvery { printerApi.deletePrinter(any()) } throws exception

        // When
        val result = printerRepository.deletePrinter(1)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }
}