package dev.giuseppedarro.comanda.features.printers.data.repository

import dev.giuseppedarro.comanda.core.domain.TokenRepository
import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.printers.data.remote.PrinterApi
import dev.giuseppedarro.comanda.features.printers.data.remote.dto.CreatePrinterRequest
import dev.giuseppedarro.comanda.features.printers.data.remote.dto.PrinterDto
import dev.giuseppedarro.comanda.features.printers.data.remote.dto.UpdatePrinterRequest
import dev.giuseppedarro.comanda.features.printers.data.remote.dto.toDomain
import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrinterRepository

class PrinterRepositoryImpl(
    private val printerApi: PrinterApi,
    private val tokenRepository: TokenRepository
) : PrinterRepository {

    override suspend fun getAllPrinters(): Result<List<Printer>> {
        return try {
            val token = tokenRepository.getAccessToken()
            if (token == null) {
                return Result.Error("User not authenticated")
            }

            val printerDtos = printerApi.getAllPrinters(token)
            Result.Success(printerDtos.map { it.toDomain() })
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun createPrinter(name: String, address: String, port: Int): Result<Printer> {
        return try {
            val token = tokenRepository.getAccessToken()
            if (token == null) {
                return Result.Error("User not authenticated")
            }

            val request = CreatePrinterRequest(name, address, port)
            val printerDto = printerApi.createPrinter(token, request)
            Result.Success(printerDto.toDomain())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun updatePrinter(id: Int, name: String, address: String, port: Int): Result<Printer> {
        return try {
            val token = tokenRepository.getAccessToken()
            if (token == null) {
                return Result.Error("User not authenticated")
            }

            val request = UpdatePrinterRequest(name, address, port)
            val printerDto = printerApi.updatePrinter(token, id, request)
            Result.Success(printerDto.toDomain())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "An unknown error occurred")
        }
    }

    override suspend fun deletePrinter(id: Int): Result<Unit> {
        return try {
            val token = tokenRepository.getAccessToken()
            if (token == null) {
                return Result.Error("User not authenticated")
            }

            printerApi.deletePrinter(token, id)
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "An unknown error occurred")
        }
    }
}