package dev.giuseppedarro.comanda.features.menu.domain.usecase

import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository

class CreateCategoryUseCase(private val repository: MenuRepository) {
    suspend operator fun invoke(category: MenuCategory): Result<Unit> {
        return repository.createCategory(category)
    }
}