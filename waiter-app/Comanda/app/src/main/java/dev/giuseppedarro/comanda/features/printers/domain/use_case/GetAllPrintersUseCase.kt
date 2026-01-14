package dev.giuseppedarro.comanda.features.printers.domain.use_case

import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrinterRepository

class GetAllPrintersUseCase(private val repository: PrinterRepository) {

    suspend operator fun invoke(): Result<List<Printer>> {
        return repository.getAllPrinters()
    }
}