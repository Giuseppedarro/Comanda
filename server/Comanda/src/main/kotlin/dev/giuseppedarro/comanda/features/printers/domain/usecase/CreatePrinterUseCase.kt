package dev.giuseppedarro.comanda.features.printers.domain.usecase

import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrintersRepository

class CreatePrinterUseCase(
    private val printersRepository: PrintersRepository
) {
    suspend operator fun invoke(name: String, address: String, port: Int): Printer {
        return printersRepository.createPrinter(name, address, port)
    }
}