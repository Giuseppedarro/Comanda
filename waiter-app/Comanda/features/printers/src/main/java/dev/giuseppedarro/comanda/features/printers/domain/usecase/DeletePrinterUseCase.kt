package dev.giuseppedarro.comanda.features.printers.domain.usecase

import dev.giuseppedarro.comanda.features.printers.domain.repository.PrinterRepository

class DeletePrinterUseCase(private val repository: PrinterRepository) {

    suspend operator fun invoke(id: Int): Result<Unit> {
        return repository.deletePrinter(id)
    }
}