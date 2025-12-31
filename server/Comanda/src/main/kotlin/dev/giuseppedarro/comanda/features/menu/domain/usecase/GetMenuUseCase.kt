package dev.giuseppedarro.comanda.features.menu.domain.usecase

import dev.giuseppedarro.comanda.features.menu.data.model.MenuCategoryResponse
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository

class GetMenuUseCase(private val repository: MenuRepository) {
    suspend operator fun invoke(): Result<List<MenuCategoryResponse>> {
        return try {
            val menu = repository.getMenu()
            Result.success(menu)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
