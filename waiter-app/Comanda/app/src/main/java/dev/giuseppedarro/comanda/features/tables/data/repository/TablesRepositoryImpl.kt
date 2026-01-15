package dev.giuseppedarro.comanda.features.tables.data.repository

import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.features.tables.domain.repository.TablesRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TablesRepositoryImpl(
    private val client: HttpClient
) : TablesRepository {
    override fun getTables(): Flow<List<Table>> = flow {
        try {
            // Token is automatically injected by Ktor Auth plugin
            val tables = client.get("tables").body<List<Table>>()
            emit(tables)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList()) // Emit an empty list on error to avoid crashing the UI
        }
    }

    override suspend fun addTable(): Result<Unit> {
        return try {
            // Token is automatically injected by Ktor Auth plugin
            client.post("tables")
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "An unknown error occurred")
        }
    }
}
