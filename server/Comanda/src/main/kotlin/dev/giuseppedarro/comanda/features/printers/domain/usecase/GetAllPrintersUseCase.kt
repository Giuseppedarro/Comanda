package dev.giuseppedarro.comanda.features.printers.domain.usecase

import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrintersRepository

class GetAllPrintersUseCase(
    private val printersRepository: PrintersRepository
) {
    suspend operator fun invoke(): List<Printer> = printersRepository.getAllPrinters()
}