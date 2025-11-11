package dev.giuseppedarro.comanda.features.tables.domain.repository

import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import kotlinx.coroutines.flow.Flow

interface TablesRepository {
    fun getTables(): Flow<List<Table>>
    suspend fun addTable(): Result<Unit>
}
