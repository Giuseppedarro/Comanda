package dev.giuseppedarro.comanda.features.printers.domain.use_case

import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrinterRepository

class UpdatePrinterUseCase(private val repository: PrinterRepository) {

    suspend operator fun invoke(id: Int, name: String, address: String, port: Int): Result<Printer> {
        return repository.updatePrinter(id, name, address, port)
    }
}