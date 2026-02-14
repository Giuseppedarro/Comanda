package dev.giuseppedarro.comanda.features.menu.data.repository

import dev.giuseppedarro.comanda.features.menu.data.remote.MenuApi
import dev.giuseppedarro.comanda.features.menu.data.remote.dto.toCreateRequest
import dev.giuseppedarro.comanda.features.menu.data.remote.dto.toDomain
import dev.giuseppedarro.comanda.features.menu.data.remote.dto.toDto
import dev.giuseppedarro.comanda.features.menu.data.remote.dto.toUpdateRequest
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class MenuRepositoryImpl(
    private val api: MenuApi
) : MenuRepository {

    override fun getMenu(): Flow<Result<List<MenuCategory>>> = flow {
        try {
            val categories = api.getMenu().map { it.toDomain() }
            emit(Result.success(categories))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.catch { e ->
        emit(Result.failure(e))
    }

    override suspend fun createCategory(category: MenuCategory): Result<Unit> = try {
        api.createCategory(category.toDto())
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateCategory(category: MenuCategory): Result<Unit> = try {
        api.updateCategory(category.id, category.toDto())
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteCategory(categoryId: String): Result<Unit> = try {
        api.deleteCategory(categoryId)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun addMenuItem(categoryId: String, item: MenuItem): Result<Unit> = try {
        api.addMenuItem(categoryId, item.toCreateRequest())
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateMenuItem(item: MenuItem): Result<Unit> = try {
        api.updateMenuItem(item.id, item.toUpdateRequest())
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteMenuItem(itemId: String): Result<Unit> = try {
        api.deleteMenuItem(itemId)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}