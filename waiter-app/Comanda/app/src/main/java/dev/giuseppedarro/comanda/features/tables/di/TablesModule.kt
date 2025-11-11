package dev.giuseppedarro.comanda.features.tables.di

import dev.giuseppedarro.comanda.features.tables.data.repository.TablesRepositoryImpl
import dev.giuseppedarro.comanda.features.tables.domain.repository.TablesRepository
import dev.giuseppedarro.comanda.features.tables.domain.use_case.AddTableUseCase
import dev.giuseppedarro.comanda.features.tables.domain.use_case.GetTablesUseCase
import dev.giuseppedarro.comanda.features.tables.presentation.TableOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val tablesModule = module {
    factoryOf(::GetTablesUseCase)
    factoryOf(::AddTableUseCase)
    factory { TablesRepositoryImpl(get(), get()) } bind TablesRepository::class

    viewModelOf(::TableOverviewViewModel)
}
