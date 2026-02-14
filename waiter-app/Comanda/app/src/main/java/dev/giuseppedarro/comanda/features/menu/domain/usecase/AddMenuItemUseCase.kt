package dev.giuseppedarro.comanda.features.menu.domain.usecase

import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository

class AddMenuItemUseCase(private val repository: MenuRepository) {
    suspend operator fun invoke(categoryId: String, item: MenuItem): Result<Unit> {
        return repository.addMenuItem(categoryId, item)
    }
}