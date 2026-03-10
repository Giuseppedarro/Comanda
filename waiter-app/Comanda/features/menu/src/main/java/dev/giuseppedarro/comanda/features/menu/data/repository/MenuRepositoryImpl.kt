package dev.giuseppedarro.comanda.features.menu.data.repository

import dev.giuseppedarro.comanda.core.data.repository.safeApiCall
import dev.giuseppedarro.comanda.core.data.repository.toDomainException
import dev.giuseppedarro.comanda.core.network.MenuApi
import dev.giuseppedarro.comanda.features.menu.data.remote.dto.toCreateRequest
import dev.giuseppedarro.comanda.features.menu.data.remote.dto.toDomain
import dev.giuseppedarro.comanda.features.menu.data.remote.dto.toDto
import dev.giuseppedarro.comanda.features.menu.data.remote.dto.toUpdateRequest
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuException
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class MenuRepositoryImpl(
    private val api: MenuApi
) : MenuRepository {

    override fun getMenu(): Flow<Result<List<MenuCategory>>> = flow {
        val categories = api.getMenu().map { it.toDomain() }
        emit(Result.success(categories))
    }.catch { e ->
        emit(Result.failure(e.toDomainException()))
    }

    override suspend fun createCategory(category: MenuCategory): Result<Unit> = 
        safeMenuApiCall { api.createCategory(category.toDto()) }

    override suspend fun updateCategory(category: MenuCategory): Result<Unit> = 
        safeMenuApiCall { api.updateCategory(category.id, category.toDto()) }

    override suspend fun deleteCategory(categoryId: String): Result<Unit> = 
        safeMenuApiCall { api.deleteCategory(categoryId) }

    override suspend fun addMenuItem(categoryId: String, item: MenuItem): Result<Unit> = 
        safeMenuApiCall { api.addMenuItem(categoryId, item.toCreateRequest()) }

    override suspend fun updateMenuItem(item: MenuItem): Result<Unit> = 
        safeMenuApiCall { api.updateMenuItem(item.id, item.toUpdateRequest()) }

    override suspend fun deleteMenuItem(itemId: String): Result<Unit> = 
        safeMenuApiCall { api.deleteMenuItem(itemId) }

    private suspend fun <T> safeMenuApiCall(call: suspend () -> T): Result<T> {
        return try {
            Result.success(call())
        } catch (e: ClientRequestException) {
            val domainError = when (e.response.status) {
                HttpStatusCode.Conflict -> MenuException.DuplicateName
                HttpStatusCode.BadRequest -> MenuException.CategoryNotEmpty // Assuming server returns 400 for not empty
                else -> e.toDomainException()
            }
            Result.failure(domainError)
        } catch (e: Exception) {
            Result.failure(e.toDomainException())
        }
    }
}
