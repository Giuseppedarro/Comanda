package dev.giuseppedarro.comanda.features.tables.data.repository

import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.features.tables.domain.repository.TablesRepository

class TablesRepositoryImpl : TablesRepository {
    // In-memory state of tables; initialized to match previous behavior (even indexes occupied)
    private val tables: MutableList<Table> = MutableList(20) { Table(number = it + 1, isOccupied = it % 2 == 0) }

    override suspend fun getTables(): List<Table> {
        return tables.toList()
    }

    override suspend fun setTableOccupied(tableNumber: Int, occupied: Boolean): Table? {
        val index = tables.indexOfFirst { it.number == tableNumber }
        if (index == -1) return null
        val current = tables[index]
        if (current.isOccupied == occupied) return current
        val updated = current.copy(isOccupied = occupied)
        tables[index] = updated
        return updated
    }
}
