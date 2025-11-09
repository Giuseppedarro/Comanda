package dev.giuseppedarro.comanda.features.orders.domain.use_case

import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow

class GetOrdersForTableUseCase(private val repository: OrderRepository) {

    operator fun invoke(tableNumber: Int): Flow<Result<List<OrderItem>>> {
        return repository.getOrdersForTable(tableNumber)
    }
}
