package dev.giuseppedarro.comanda.features.orders.domain.usecase

import dev.giuseppedarro.comanda.features.orders.domain.repository.OrdersRepository

class GetOrderByIdUseCase(private val ordersRepository: OrdersRepository) {
    suspend operator fun invoke(orderId: String) = ordersRepository.getOrderById(orderId)
}
