package dev.giuseppedarro.comanda.features.printers.presentation

import dev.giuseppedarro.comanda.core.domain.model.DomainException
import dev.giuseppedarro.comanda.core.presentation.UiText
import dev.giuseppedarro.comanda.core.presentation.toUiText
import dev.giuseppedarro.comanda.features.printers.R
import dev.giuseppedarro.comanda.features.printers.domain.model.PrinterException

fun Throwable.toPrinterUiText(): UiText {
    return when (this) {
        is PrinterException.PrinterNotFound -> UiText.StringResource(R.string.error_printer_not_found)
        is PrinterException.DuplicatePrinter -> UiText.StringResource(R.string.error_duplicate_printer)
        is DomainException -> this.toUiText()
        else -> UiText.DynamicString(this.message ?: "Unknown error")
    }
}
