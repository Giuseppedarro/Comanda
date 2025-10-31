package dev.giuseppedarro.comanda.features.orders.domain.usecase

import dev.giuseppedarro.comanda.features.orders.domain.repository.OrdersRepository

class GetOrdersUseCase(private val ordersRepository: OrdersRepository) {
    suspend operator fun invoke() = ordersRepository.getOrders()
}
