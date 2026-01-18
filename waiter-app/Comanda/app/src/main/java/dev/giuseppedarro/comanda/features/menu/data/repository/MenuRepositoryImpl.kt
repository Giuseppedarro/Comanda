package dev.giuseppedarro.comanda.features.menu.data.repository

import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.menu.data.remote.MenuApi
import dev.giuseppedarro.comanda.features.menu.data.remote.dto.toDomain
import dev.giuseppedarro.comanda.features.menu.data.remote.dto.toDto
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class MenuRepositoryImpl(
    private val api: MenuApi
) : MenuRepository {

    override fun getMenu(): Flow<Result<List<dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory>>> = flow {
        emit(Result.Loading())
        try {
            val categories = api.getMenu().map { it.toDomain() }
            emit(Result.Success(categories))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }.catch { e ->
        emit(Result.Error(e.message ?: "Unknown error"))
    }

    override suspend fun addMenuItem(categoryName: String, item: MenuItem): Result<Unit> = try {
        api.addMenuItem(categoryName, item.toDto())
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.message ?: "Unknown error")
    }

    override suspend fun updateMenuItem(categoryName: String, item: MenuItem): Result<Unit> = try {
        api.updateMenuItem(categoryName, item.id, item.toDto())
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.message ?: "Unknown error")
    }

    override suspend fun deleteMenuItem(categoryName: String, itemId: String): Result<Unit> = try {
        api.deleteMenuItem(categoryName, itemId)
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.message ?: "Unknown error")
    }
}