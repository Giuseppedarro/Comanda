package dev.giuseppedarro.comanda.features.tables.domain.usecase

import dev.giuseppedarro.comanda.features.tables.domain.repository.TablesRepository

class GetTablesUseCase(private val tablesRepository: TablesRepository) {
    suspend operator fun invoke() = tablesRepository.getTables()
}
