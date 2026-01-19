package dev.giuseppedarro.comanda.features.menu.domain.usecase

import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository

class AddCategoryUseCase(private val repository: MenuRepository) {
    suspend operator fun invoke(category: MenuCategory): Result<Unit> {
        if (category.name.isBlank()) return Result.failure(IllegalArgumentException("Category name cannot be empty"))
        return repository.addCategory(category)
    }
}