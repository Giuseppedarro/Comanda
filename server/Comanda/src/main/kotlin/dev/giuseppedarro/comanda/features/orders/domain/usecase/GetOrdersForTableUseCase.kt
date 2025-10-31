package dev.giuseppedarro.comanda.features.orders.domain.usecase

import dev.giuseppedarro.comanda.features.orders.domain.repository.OrdersRepository

class GetOrdersForTableUseCase(private val ordersRepository: OrdersRepository) {
    suspend operator fun invoke(tableNumber: Int) = ordersRepository.getOrdersForTable(tableNumber)
}
