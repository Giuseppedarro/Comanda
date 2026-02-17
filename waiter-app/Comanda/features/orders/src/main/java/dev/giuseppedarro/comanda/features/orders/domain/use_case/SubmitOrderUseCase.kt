package dev.giuseppedarro.comanda.features.orders.domain.use_case

import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrderRepository

class SubmitOrderUseCase(private val repository: OrderRepository) {

    suspend operator fun invoke(tableNumber: Int, numberOfPeople: Int, items: List<OrderItem>): Result<Unit> {
        return repository.submitOrder(tableNumber, numberOfPeople, items)
    }
}
