package dev.giuseppedarro.comanda.features.printers.domain.use_case

import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrinterRepository

class CreatePrinterUseCase(private val repository: PrinterRepository) {

    suspend operator fun invoke(name: String, address: String, port: Int): Result<Printer> {
        return repository.createPrinter(name, address, port)
    }
}