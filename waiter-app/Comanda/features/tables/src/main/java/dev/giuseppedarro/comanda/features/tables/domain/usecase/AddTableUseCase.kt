package dev.giuseppedarro.comanda.features.tables.domain.usecase

import dev.giuseppedarro.comanda.features.tables.domain.repository.TablesRepository

class AddTableUseCase(private val repository: TablesRepository) {
    suspend operator fun invoke(): Result<Unit> = repository.addTable()
}
