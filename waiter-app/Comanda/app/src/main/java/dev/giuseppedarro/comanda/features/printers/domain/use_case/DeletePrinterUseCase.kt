package dev.giuseppedarro.comanda.features.printers.domain.use_case

import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrinterRepository

class DeletePrinterUseCase(private val repository: PrinterRepository) {

    suspend operator fun invoke(id: Int): Result<Unit> {
        return repository.deletePrinter(id)
    }
}