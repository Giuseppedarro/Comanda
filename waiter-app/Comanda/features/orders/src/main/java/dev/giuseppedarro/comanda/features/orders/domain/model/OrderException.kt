package dev.giuseppedarro.comanda.features.orders.domain.model

import dev.giuseppedarro.comanda.core.domain.model.DomainException

sealed class OrderException(message: String? = null) : DomainException(message) {
    object OrderNotFound : OrderException("The requested order was not found.")
    object InvalidOrderState : OrderException("The action cannot be performed in the current order state.")
    object EmptyOrder : OrderException("Cannot confirm an empty order.")
    object TableOccupied : OrderException("The table is already occupied by another active order.")
}
