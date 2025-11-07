package dev.giuseppedarro.comanda.features.orders.domain.usecase

import dev.giuseppedarro.comanda.features.orders.data.model.SubmitOrderRequest
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrdersRepository
import dev.giuseppedarro.comanda.features.tables.domain.repository.TablesRepository

class SubmitOrderUseCase(
    private val ordersRepository: OrdersRepository,
    private val tablesRepository: TablesRepository,
) {
    suspend operator fun invoke(request: SubmitOrderRequest) =
        ordersRepository.submitOrder(request).onSuccess {
            // When a new order is created, mark the table as occupied
            tablesRepository.setTableOccupied(request.tableNumber, true)
        }
}
