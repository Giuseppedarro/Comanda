package dev.giuseppedarro.comanda.features.printers.domain.repository

import dev.giuseppedarro.comanda.features.printers.domain.model.Printer

interface PrintersRepository {
    suspend fun getAllPrinters(): List<Printer>
    suspend fun getPrinterById(id: Int): Printer?
    suspend fun createPrinter(name: String, address: String, port: Int): Printer
    suspend fun updatePrinter(id: Int, name: String, address: String, port: Int): Printer?
    suspend fun deletePrinter(id: Int): Boolean
}