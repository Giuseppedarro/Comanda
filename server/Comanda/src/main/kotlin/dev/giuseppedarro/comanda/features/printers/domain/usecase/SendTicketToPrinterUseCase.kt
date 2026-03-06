package dev.giuseppedarro.comanda.features.printers.domain.usecase

import dev.giuseppedarro.comanda.features.printers.domain.repository.PrintersRepository
import dev.giuseppedarro.comanda.features.printers.domain.service.PrinterService

/**
 * Sends a formatted ticket text to a specific printer.
 * This is used when orders are submitted and tickets need to be printed.
 *
 * @param printerId The ID of the printer to send the ticket to
 * @param ticketContent The formatted ticket text (e.g., order details, items, table number)
 * @return Result containing success/failure information
 */
class SendTicketToPrinterUseCase(
    private val printersRepository: PrintersRepository,
    private val printerService: PrinterService
) {
    suspend operator fun invoke(printerId: Int, ticketContent: String): Result<Unit> {
        return try {
            // Get printer details
            val printer = printersRepository.getPrinterById(printerId)
                ?: return Result.failure(IllegalArgumentException("Printer not found with ID: $printerId"))

            // Send ticket using the service
            printerService.printTicket(printer.address, printer.port, ticketContent)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
