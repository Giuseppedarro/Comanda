package dev.giuseppedarro.comanda.features.tables.data.repository

import dev.giuseppedarro.comanda.features.tables.data.Tables
import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.features.tables.domain.repository.TablesRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class TablesRepositoryImpl : TablesRepository {

    override suspend fun getTables(): List<Table> {
        return transaction {
            // Some environments may not have the Exposed orderBy extension available.
            // Fetch all rows and sort in Kotlin to avoid the dependency on orderBy.
            Tables.selectAll()
                .map {
                    Table(
                        number = it[Tables.number],
                        isOccupied = it[Tables.isOccupied]
                    )
                }
                .sortedBy { it.number }
        }
    }

    override suspend fun setTableOccupied(tableNumber: Int, occupied: Boolean): Table? {
        return transaction {
            val updatedCount = Tables.update({ Tables.number eq tableNumber }) {
                it[isOccupied] = occupied
            }
            
            if (updatedCount > 0) {
                Table(number = tableNumber, isOccupied = occupied)
            } else {
                null
            }
        }
    }

    override suspend fun addTable(): Table {
        return transaction {
            val maxNumber = Tables.selectAll().maxOfOrNull { it[Tables.number] } ?: 0
            val newNumber = maxNumber + 1
            
            Tables.insert {
                it[number] = newNumber
                it[isOccupied] = false
            }
            
            Table(number = newNumber, isOccupied = false)
        }
    }
}
