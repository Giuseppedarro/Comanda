package dev.giuseppedarro.comanda.features.printers.data.repository

import dev.giuseppedarro.comanda.features.printers.data.remote.PrinterApi
import dev.giuseppedarro.comanda.features.printers.data.remote.dto.CreatePrinterRequest
import dev.giuseppedarro.comanda.features.printers.data.remote.dto.UpdatePrinterRequest
import dev.giuseppedarro.comanda.features.printers.data.remote.dto.toDomain
import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrinterRepository

class PrinterRepositoryImpl(
    private val printerApi: PrinterApi
) : PrinterRepository {

    override suspend fun getAllPrinters(): Result<List<Printer>> {
        return try {
            val printerDtos = printerApi.getAllPrinters()
            Result.success(printerDtos.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createPrinter(name: String, address: String, port: Int): Result<Printer> {
        return try {
            val request = CreatePrinterRequest(name, address, port)
            val printerDto = printerApi.createPrinter(request)
            Result.success(printerDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePrinter(id: Int, name: String, address: String, port: Int): Result<Printer> {
        return try {
            val request = UpdatePrinterRequest(name, address, port)
            val printerDto = printerApi.updatePrinter(id, request)
            Result.success(printerDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deletePrinter(id: Int): Result<Unit> {
        return try {
            printerApi.deletePrinter(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}