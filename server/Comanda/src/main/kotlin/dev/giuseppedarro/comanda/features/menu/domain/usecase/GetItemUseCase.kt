package dev.giuseppedarro.comanda.features.menu.domain.usecase

import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository

class GetItemUseCase(private val repository: MenuRepository) {
    suspend operator fun invoke(itemId: String): Result<MenuItem> {
        return repository.getItem(itemId)
    }
}