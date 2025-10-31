package dev.giuseppedarro.comanda.features.orders.domain.use_case

import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrderRepository

class SubmitOrderUseCase(private val repository: OrderRepository) {

    suspend operator fun invoke(tableNumber: Int, items: List<OrderItem>): Result<Unit> {
        // In the future, you could add validation logic here, e.g., check if the items list is not empty.
        return repository.submitOrder(tableNumber, items)
    }
}
