package dev.giuseppedarro.comanda.features.printers.domain.usecase

import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrintersRepository

class UpdatePrinterUseCase(
    private val printersRepository: PrintersRepository
) {
    suspend operator fun invoke(id: Int, name: String, address: String, port: Int): Printer? {
        return printersRepository.updatePrinter(id, name, address, port)
    }
}