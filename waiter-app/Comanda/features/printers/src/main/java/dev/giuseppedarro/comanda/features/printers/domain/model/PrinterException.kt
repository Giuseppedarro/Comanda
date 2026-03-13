package dev.giuseppedarro.comanda.features.printers.domain.model

import dev.giuseppedarro.comanda.core.domain.model.DomainException

sealed class PrinterException(message: String? = null) : DomainException(message) {
    object PrinterNotFound : PrinterException("The requested printer was not found.")
    object DuplicatePrinter : PrinterException("A printer with this name or address already exists.")
}
