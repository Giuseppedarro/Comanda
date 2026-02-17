package dev.giuseppedarro.comanda.features.tables.data.repository

import dev.giuseppedarro.comanda.features.tables.data.remote.TableApi
import dev.giuseppedarro.comanda.features.tables.data.remote.dto.toDomain
import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.features.tables.domain.repository.TablesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TablesRepositoryImpl(
    private val tableApi: TableApi
) : TablesRepository {
    override fun getTables(): Flow<List<Table>> = flow {
        try {
            // Token is automatically injected by Ktor Auth plugin
            val tableDtos = tableApi.getTables()
            emit(tableDtos.map { it.toDomain() })
        } catch (e: Exception) {
            emit(emptyList()) // Emit an empty list on error to avoid crashing the UI
        }
    }

    override suspend fun addTable(): Result<Unit> {
        return try {
            // Token is automatically injected by Ktor Auth plugin
            tableApi.addTable()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
