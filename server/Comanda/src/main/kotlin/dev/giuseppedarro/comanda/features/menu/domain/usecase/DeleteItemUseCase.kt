package dev.giuseppedarro.comanda.features.menu.domain.usecase

import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository

class DeleteItemUseCase(private val repository: MenuRepository) {
    suspend operator fun invoke(itemId: String): Result<Unit> {
        return repository.deleteItem(itemId)
    }
}