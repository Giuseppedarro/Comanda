package dev.giuseppedarro.comanda.features.orders.presentation

import dev.giuseppedarro.comanda.core.domain.model.DomainException
import dev.giuseppedarro.comanda.core.presentation.UiText
import dev.giuseppedarro.comanda.core.presentation.toUiText
import dev.giuseppedarro.comanda.features.orders.R
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderException

fun Throwable.toOrderUiText(): UiText {
    return when (this) {
        is OrderException.OrderNotFound -> UiText.StringResource(R.string.error_order_not_found)
        is OrderException.InvalidOrderState -> UiText.StringResource(R.string.error_invalid_order_state)
        is OrderException.EmptyOrder -> UiText.StringResource(R.string.error_empty_order)
        is OrderException.TableOccupied -> UiText.StringResource(R.string.error_table_occupied)
        is DomainException -> this.toUiText()
        else -> UiText.DynamicString(this.message ?: "Unknown error")
    }
}
