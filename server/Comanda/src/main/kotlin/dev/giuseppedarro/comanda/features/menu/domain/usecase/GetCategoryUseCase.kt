package dev.giuseppedarro.comanda.features.menu.domain.usecase

import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository

class GetCategoryUseCase(private val repository: MenuRepository) {
    suspend operator fun invoke(categoryId: String): Result<MenuCategory> {
        return repository.getCategory(categoryId)
    }
}