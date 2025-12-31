package dev.giuseppedarro.comanda.features.payments.domain.usecase

import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderStatus
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrdersRepository
import dev.giuseppedarro.comanda.features.payments.data.model.BillItem
import dev.giuseppedarro.comanda.features.payments.data.model.BillResponse

class GetBillUseCase(
    private val ordersRepository: OrdersRepository,
    private val menuRepository: MenuRepository
) {
    suspend operator fun invoke(tableNumber: Int): Result<BillResponse> {
        val orders = ordersRepository.getOrdersForTable(tableNumber)
        // Assuming there is only one open order per table for now
        val activeOrder = orders.find { it.status == OrderStatus.open || it.status == OrderStatus.served }
            ?: return Result.failure(Exception("No active order found for table $tableNumber"))

        val billItems = activeOrder.items.map { item ->
            val price = menuRepository.getPrice(item.itemId)
            val name = menuRepository.getName(item.itemId)
            BillItem(
                name = name,
                quantity = item.quantity,
                price = price,
                total = price * item.quantity
            )
        }

        val subtotal = billItems.sumOf { it.total }
        val serviceCharge = subtotal * 0.10 // 10% service charge
        val total = subtotal + serviceCharge

        return Result.success(
            BillResponse(
                orderId = activeOrder.id,
                tableNumber = tableNumber,
                items = billItems,
                subtotal = subtotal,
                serviceCharge = serviceCharge,
                total = total
            )
        )
    }
}
