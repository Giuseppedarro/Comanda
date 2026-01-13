package dev.giuseppedarro.comanda.features.printers.domain.usecase

import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrintersRepository

/**
 * Sends a formatted ticket text to a specific printer.
 * This is used when orders are submitted and tickets need to be printed.
 *
 * @param printerId The ID of the printer to send the ticket to
 * @param ticketContent The formatted ticket text (e.g., order details, items, table number)
 * @return Result containing success/failure information
 */
class SendTicketToPrinterUseCase(
    private val printersRepository: PrintersRepository
) {
    suspend operator fun invoke(printerId: Int, ticketContent: String): Result<Unit> {
        return try {
            // Get printer details
            val printer = printersRepository.getPrinterById(printerId)
                ?: return Result.failure(IllegalArgumentException("Printer not found with ID: $printerId"))

            // TODO: Send actual ticket to printer via socket/ESC/POS
            // This will be implemented in the data layer
            sendToPrinter(printer, ticketContent)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun sendToPrinter(printer: Printer, ticketContent: String) {
        // Placeholder - actual implementation will use socket communication
        // This will be moved to a printer client in the data layer
        println("Sending ticket to printer:")
        println("  Printer: ${printer.name} (${printer.address}:${printer.port})")
        println("  Content:")
        println(ticketContent)
    }
}