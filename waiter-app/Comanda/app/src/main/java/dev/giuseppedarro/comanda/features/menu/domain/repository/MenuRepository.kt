package dev.giuseppedarro.comanda.features.menu.domain.repository

import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import kotlinx.coroutines.flow.Flow

interface MenuRepository {

    /**
     * Fetches the full menu (all categories and items) from the data source.
     */
    fun getMenu(): Flow<Result<List<MenuCategory>>>

    /**
     * Adds a new menu item to a specific category.
     */
    suspend fun addMenuItem(categoryName: String, item: MenuItem): Result<Unit>

    /**
     * Updates an existing menu item.
     */
    suspend fun updateMenuItem(categoryName: String, item: MenuItem): Result<Unit>

    /**
     * Deletes a menu item from a specific category.
     */
    suspend fun deleteMenuItem(categoryName: String, itemId: String): Result<Unit>
}