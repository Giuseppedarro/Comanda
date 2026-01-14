package dev.giuseppedarro.comanda.features.printers.domain.repository

import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.printers.domain.model.Printer

interface PrinterRepository {
    suspend fun getAllPrinters(): Result<List<Printer>>
    suspend fun createPrinter(name: String, address: String, port: Int): Result<Printer>
    suspend fun updatePrinter(id: Int, name: String, address: String, port: Int): Result<Printer>
    suspend fun deletePrinter(id: Int): Result<Unit>
}