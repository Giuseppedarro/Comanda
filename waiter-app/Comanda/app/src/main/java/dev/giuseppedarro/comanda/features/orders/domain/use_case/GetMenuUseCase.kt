package dev.giuseppedarro.comanda.features.orders.domain.use_case

import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.orders.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow

class GetMenuUseCase(private val repository: OrderRepository) {

    operator fun invoke(): Flow<Result<List<MenuCategory>>> {
        return repository.getMenu()
    }
}
