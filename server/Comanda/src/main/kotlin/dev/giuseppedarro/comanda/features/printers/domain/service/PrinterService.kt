package dev.giuseppedarro.comanda.features.printers.domain.service

interface PrinterService {
    suspend fun printTicket(ip: String, port: Int, content: String): Result<Unit>
}
