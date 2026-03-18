package dev.giuseppedarro.comanda.features.tables.domain.usecase

import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.features.tables.domain.repository.TablesRepository

class AddTableUseCase(private val tablesRepository: TablesRepository) {
    suspend operator fun invoke(number: Int? = null): Result<Table> = tablesRepository.addTable(number)
}
