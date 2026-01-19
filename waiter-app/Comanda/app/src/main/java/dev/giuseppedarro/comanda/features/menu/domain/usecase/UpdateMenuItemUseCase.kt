package dev.giuseppedarro.comanda.features.menu.domain.usecase

import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository

class UpdateMenuItemUseCase(private val repository: MenuRepository) {
    suspend operator fun invoke(item: MenuItem): Result<Unit> {
        return repository.updateMenuItem(item)
    }
}