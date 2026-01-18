package dev.giuseppedarro.comanda.features.menu.domain.usecase

import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository

class DeleteMenuItemUseCase(private val repository: MenuRepository) {
    suspend operator fun invoke(categoryName: String, itemId: String): Result<Unit> {
        return repository.deleteMenuItem(categoryName, itemId)
    }
}