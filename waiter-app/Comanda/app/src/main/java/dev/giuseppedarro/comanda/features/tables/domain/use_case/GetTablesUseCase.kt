package dev.giuseppedarro.comanda.features.tables.domain.use_case

import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.features.tables.domain.repository.TablesRepository
import kotlinx.coroutines.flow.Flow

class GetTablesUseCase(private val repository: TablesRepository) {

    operator fun invoke(): Flow<List<Table>> {
        return repository.getTables()
    }
}
