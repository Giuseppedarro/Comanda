package dev.giuseppedarro.comanda.features.menu.domain.usecase

import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository

class GetMenuUseCase(
    private val menuRepository: MenuRepository
) {
    suspend operator fun invoke(): List<MenuCategory> {
        return menuRepository.getMenu()
    }
}