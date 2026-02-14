package dev.giuseppedarro.comanda.features.menu.domain.usecase

import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository

class DeleteCategoryUseCase(private val repository: MenuRepository) {
    suspend operator fun invoke(categoryId: String): Result<Unit> {
        return repository.deleteCategory(categoryId)
    }
}