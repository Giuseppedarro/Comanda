package dev.giuseppedarro.comanda.features.menu.domain.usecase

import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository

class AddMenuItemUseCase(private val repository: MenuRepository) {
    suspend operator fun invoke(categoryName: String, item: MenuItem): Result<Unit> {
        return repository.addMenuItem(categoryName, item)
    }
}