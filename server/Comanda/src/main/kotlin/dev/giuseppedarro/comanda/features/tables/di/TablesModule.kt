package dev.giuseppedarro.comanda.features.tables.di

import dev.giuseppedarro.comanda.features.tables.data.repository.TablesRepositoryImpl
import dev.giuseppedarro.comanda.features.tables.domain.repository.TablesRepository
import dev.giuseppedarro.comanda.features.tables.domain.usecase.GetTablesUseCase
import org.koin.dsl.module

val tablesModule = module {
    single<TablesRepository> { TablesRepositoryImpl() }
    single { GetTablesUseCase(get()) }
}
