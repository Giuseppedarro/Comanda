package dev.giuseppedarro.comanda.features.menu.domain.repository

import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem

interface MenuRepository {
    suspend fun getMenu(): List<MenuCategory>
    suspend fun updateMenu(categories: List<MenuCategory>): Result<Unit>

    // Category CRUD
    suspend fun addCategory(category: MenuCategory): Result<Unit>
    suspend fun updateCategory(category: MenuCategory): Result<Unit>
    suspend fun deleteCategory(categoryId: String): Result<Unit>
    suspend fun getCategory(categoryId: String): Result<MenuCategory>

    // Item CRUD
    suspend fun addItem(item: MenuItem): Result<Unit>
    suspend fun updateItem(item: MenuItem): Result<Unit>
    suspend fun deleteItem(itemId: String): Result<Unit>
    suspend fun getItem(itemId: String): Result<MenuItem>
}