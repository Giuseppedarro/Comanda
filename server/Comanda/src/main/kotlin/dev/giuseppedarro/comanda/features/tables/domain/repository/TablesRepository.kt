package dev.giuseppedarro.comanda.features.tables.domain.repository

import dev.giuseppedarro.comanda.features.tables.domain.model.Table

interface TablesRepository {
    suspend fun getTables(): List<Table>
}
