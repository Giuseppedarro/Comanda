package dev.giuseppedarro.comanda.features.orders.domain.model

data class OrderItem(val menuItem: MenuItem, val quantity: Int = 1)
