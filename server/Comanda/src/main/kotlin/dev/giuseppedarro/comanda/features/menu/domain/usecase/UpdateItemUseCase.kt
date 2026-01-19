package dev.giuseppedarro.comanda.features.menu.domain.usecase

import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository

class UpdateItemUseCase(private val repository: MenuRepository) {
    suspend operator fun invoke(item: MenuItem): Result<Unit> {
        if (item.name.isBlank()) return Result.failure(IllegalArgumentException("Item name cannot be empty"))
        if (item.price < 0) return Result.failure(IllegalArgumentException("Price cannot be negative"))
        return repository.updateItem(item)
    }
}