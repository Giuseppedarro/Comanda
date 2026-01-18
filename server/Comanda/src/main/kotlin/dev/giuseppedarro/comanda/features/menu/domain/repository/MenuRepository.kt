package dev.giuseppedarro.comanda.features.menu.domain.repository

import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory

interface MenuRepository {
    suspend fun getMenu(): List<MenuCategory>
    suspend fun updateMenu(categories: List<MenuCategory>): Result<Unit>
}