package dev.giuseppedarro.comanda.features.printers.data.repository

import dev.giuseppedarro.comanda.core.data.repository.toDomainException
import dev.giuseppedarro.comanda.features.printers.data.remote.PrinterApi
import dev.giuseppedarro.comanda.features.printers.data.remote.dto.CreatePrinterRequest
import dev.giuseppedarro.comanda.features.printers.data.remote.dto.UpdatePrinterRequest
import dev.giuseppedarro.comanda.features.printers.data.remote.dto.toDomain
import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.domain.model.PrinterException
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrinterRepository
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode

class PrinterRepositoryImpl(
    private val printerApi: PrinterApi
) : PrinterRepository {

    override suspend fun getAllPrinters(): Result<List<Printer>> {
        return try {
            val printerDtos = printerApi.getAllPrinters()
            Result.success(printerDtos.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e.toDomainException())
        }
    }

    override suspend fun createPrinter(name: String, address: String, port: Int): Result<Printer> {
        return try {
            val request = CreatePrinterRequest(name, address, port)
            val printerDto = printerApi.createPrinter(request)
            Result.success(printerDto.toDomain())
        } catch (e: ClientRequestException) {
            val domainError = when (e.response.status) {
                HttpStatusCode.Conflict -> PrinterException.DuplicatePrinter
                else -> e.toDomainException()
            }
            Result.failure(domainError)
        } catch (e: Exception) {
            Result.failure(e.toDomainException())
        }
    }

    override suspend fun updatePrinter(id: Int, name: String, address: String, port: Int): Result<Printer> {
        return try {
            val request = UpdatePrinterRequest(name, address, port)
            val printerDto = printerApi.updatePrinter(id, request)
            Result.success(printerDto.toDomain())
        } catch (e: ClientRequestException) {
            val domainError = when (e.response.status) {
                HttpStatusCode.NotFound -> PrinterException.PrinterNotFound
                HttpStatusCode.Conflict -> PrinterException.DuplicatePrinter
                else -> e.toDomainException()
            }
            Result.failure(domainError)
        } catch (e: Exception) {
            Result.failure(e.toDomainException())
        }
    }

    override suspend fun deletePrinter(id: Int): Result<Unit> {
        return try {
            printerApi.deletePrinter(id)
            Result.success(Unit)
        } catch (e: ClientRequestException) {
            val domainError = when (e.response.status) {
                HttpStatusCode.NotFound -> PrinterException.PrinterNotFound
                else -> e.toDomainException()
            }
            Result.failure(domainError)
        } catch (e: Exception) {
            Result.failure(e.toDomainException())
        }
    }
}
