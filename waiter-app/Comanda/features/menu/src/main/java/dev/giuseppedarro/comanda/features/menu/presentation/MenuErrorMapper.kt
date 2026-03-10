package dev.giuseppedarro.comanda.features.menu.presentation

import dev.giuseppedarro.comanda.core.domain.model.DomainException
import dev.giuseppedarro.comanda.core.presentation.UiText
import dev.giuseppedarro.comanda.core.presentation.toUiText
import dev.giuseppedarro.comanda.features.menu.R
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuException

fun Throwable.toMenuUiText(): UiText {
    return when (this) {
        is MenuException.CategoryNotEmpty -> UiText.StringResource(R.string.error_category_not_empty)
        is MenuException.DuplicateName -> UiText.StringResource(R.string.error_duplicate_name)
        is MenuException.EmptyName -> UiText.StringResource(R.string.error_empty_name)
        is MenuException.InvalidPrice -> UiText.StringResource(R.string.error_invalid_price)
        is DomainException -> this.toUiText()
        else -> UiText.DynamicString(this.message ?: "Unknown error")
    }
}
