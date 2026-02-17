package dev.giuseppedarro.comanda.features.menu.domain.usecase

import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository
import kotlinx.coroutines.flow.Flow

class GetMenuUseCase(private val repository: MenuRepository) {
    operator fun invoke(): Flow<Result<List<MenuCategory>>> = repository.getMenu()
}