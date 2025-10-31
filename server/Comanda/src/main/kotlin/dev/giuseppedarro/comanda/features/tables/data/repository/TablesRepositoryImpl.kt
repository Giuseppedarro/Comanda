package dev.giuseppedarro.comanda.features.tables.data.repository

import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.features.tables.domain.repository.TablesRepository

class TablesRepositoryImpl : TablesRepository {
    override suspend fun getTables(): List<Table> {
        return List(20) { Table(number = it + 1, isOccupied = it % 2 == 0) }
    }
}
