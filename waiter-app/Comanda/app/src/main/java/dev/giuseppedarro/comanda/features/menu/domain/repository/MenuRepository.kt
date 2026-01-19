package dev.giuseppedarro.comanda.features.menu.domain.repository

import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import kotlinx.coroutines.flow.Flow

interface MenuRepository {

    fun getMenu(): Flow<Result<List<MenuCategory>>>

    suspend fun createCategory(category: MenuCategory): Result<Unit>

    suspend fun updateCategory(category: MenuCategory): Result<Unit>

    suspend fun deleteCategory(categoryId: String): Result<Unit>

    suspend fun addMenuItem(categoryId: String, item: MenuItem): Result<Unit>

    suspend fun updateMenuItem(item: MenuItem): Result<Unit>

    suspend fun deleteMenuItem(itemId: String): Result<Unit>
}