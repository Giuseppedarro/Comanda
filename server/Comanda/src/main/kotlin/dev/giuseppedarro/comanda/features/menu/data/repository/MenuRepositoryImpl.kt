package dev.giuseppedarro.comanda.features.menu.data.repository

import dev.giuseppedarro.comanda.features.menu.data.MenuCategories
import dev.giuseppedarro.comanda.features.menu.data.MenuItems
import dev.giuseppedarro.comanda.features.menu.data.model.MenuCategoryResponse
import dev.giuseppedarro.comanda.features.menu.data.model.MenuItemResponse
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.Locale

class MenuRepositoryImpl : MenuRepository {

    override suspend fun getMenu(): List<MenuCategoryResponse> {
        return try {
            transaction {
                val categories = MenuCategories.selectAll().toList()
                
                categories.map { categoryRow ->
                    val categoryId = categoryRow[MenuCategories.id]
                    val items = MenuItems.select { MenuItems.categoryId eq categoryId }.map { itemRow ->
                        val price = itemRow[MenuItems.price]
                        MenuItemResponse(
                            id = itemRow[MenuItems.id],
                            name = itemRow[MenuItems.name],
                            price = String.format(Locale.US, "$%.2f", price)
                        )
                    }
                    
                    MenuCategoryResponse(
                        name = categoryRow[MenuCategories.name],
                        items = items
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getPrice(itemId: String): Double {
        return transaction {
            MenuItems.select { MenuItems.id eq itemId }
                .singleOrNull()
                ?.get(MenuItems.price) ?: 0.0
        }
    }

    override suspend fun getName(itemId: String): String {
        return transaction {
            MenuItems.select { MenuItems.id eq itemId }
                .singleOrNull()
                ?.get(MenuItems.name) ?: "Unknown Item"
        }
    }
}
