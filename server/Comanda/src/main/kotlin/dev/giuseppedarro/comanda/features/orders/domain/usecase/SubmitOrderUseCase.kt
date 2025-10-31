package dev.giuseppedarro.comanda.features.orders.domain.usecase

import dev.giuseppedarro.comanda.features.orders.data.model.SubmitOrderRequest
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrdersRepository

class SubmitOrderUseCase(private val ordersRepository: OrdersRepository) {
    suspend operator fun invoke(request: SubmitOrderRequest) = ordersRepository.submitOrder(request)
}
