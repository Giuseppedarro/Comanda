package dev.giuseppedarro.comanda.features.menu.domain.repository

import dev.giuseppedarro.comanda.features.menu.data.model.MenuCategoryResponse

interface MenuRepository {
    suspend fun getMenu(): List<MenuCategoryResponse>
    suspend fun getPrice(itemId: String): Double
    suspend fun getName(itemId: String): String
}
