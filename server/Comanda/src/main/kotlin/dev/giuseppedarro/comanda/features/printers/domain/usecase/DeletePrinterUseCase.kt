package dev.giuseppedarro.comanda.features.printers.domain.usecase

import dev.giuseppedarro.comanda.features.printers.domain.repository.PrintersRepository

class DeletePrinterUseCase(
    private val printersRepository: PrintersRepository
) {
    suspend operator fun invoke(id: Int): Boolean {
        return printersRepository.deletePrinter(id)
    }
}