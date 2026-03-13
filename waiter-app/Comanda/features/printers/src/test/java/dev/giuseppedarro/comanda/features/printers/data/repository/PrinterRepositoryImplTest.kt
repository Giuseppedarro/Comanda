package dev.giuseppedarro.comanda.features.printers.data.repository

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.core.domain.model.DomainException
import dev.giuseppedarro.comanda.features.printers.data.remote.PrinterApi
import dev.giuseppedarro.comanda.features.printers.data.remote.dto.PrinterDto
import dev.giuseppedarro.comanda.features.printers.domain.model.PrinterException
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrinterRepository
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

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
        val printerDtos = listOf(PrinterDto(1, "Kitchen Printer", "192.168.1.100", 9100))
        coEvery { printerApi.getAllPrinters() } returns printerDtos

        val result = printerRepository.getAllPrinters()

        assertThat(result.isSuccess).isTrue()
        val printers = result.getOrNull()
        assertThat(printers).hasSize(1)
        assertThat(printers!![0].id).isEqualTo(1)
    }

    @Test
    fun `getAllPrinters should return network exception when IOException occurs`() = runTest {
        coEvery { printerApi.getAllPrinters() } throws IOException()

        val result = printerRepository.getAllPrinters()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(DomainException.NetworkException::class.java)
    }

    @Test
    fun `createPrinter should return duplicate printer error when 409 conflict occurs`() = runTest {
        val response = mockk<HttpResponse>(relaxed = true) {
            every { status } returns HttpStatusCode.Conflict
        }
        coEvery { printerApi.createPrinter(any()) } throws ClientRequestException(response, "Conflict")

        val result = printerRepository.createPrinter("Kitchen", "1.1.1.1", 9100)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(PrinterException.DuplicatePrinter)
    }

    @Test
    fun `updatePrinter should return printer not found error when 404 occurs`() = runTest {
        val response = mockk<HttpResponse>(relaxed = true) {
            every { status } returns HttpStatusCode.NotFound
        }
        coEvery { printerApi.updatePrinter(any(), any()) } throws ClientRequestException(response, "Not Found")

        val result = printerRepository.updatePrinter(1, "Kitchen", "1.1.1.1", 9100)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(PrinterException.PrinterNotFound)
    }

    @Test
    fun `deletePrinter should return success when api is successful`() = runTest {
        coJustRun { printerApi.deletePrinter(any()) }

        val result = printerRepository.deletePrinter(1)

        assertThat(result.isSuccess).isTrue()
    }
}
