package dev.giuseppedarro.comanda.features.tables.presentation

import dev.giuseppedarro.comanda.core.domain.model.DomainException
import dev.giuseppedarro.comanda.core.presentation.UiText
import dev.giuseppedarro.comanda.core.presentation.toUiText
import dev.giuseppedarro.comanda.features.tables.R
import dev.giuseppedarro.comanda.features.tables.domain.model.TableException

fun Throwable.toTableUiText(): UiText {
    return when (this) {
        is TableException.TableNotFound -> UiText.StringResource(R.string.error_table_not_found)
        is TableException.TableAlreadyExists -> UiText.StringResource(R.string.error_table_already_exists)
        is TableException.InvalidTableNumber -> UiText.StringResource(R.string.error_invalid_table_number)
        is DomainException -> this.toUiText()
        else -> UiText.DynamicString(this.message ?: "Unknown error")
    }
}
