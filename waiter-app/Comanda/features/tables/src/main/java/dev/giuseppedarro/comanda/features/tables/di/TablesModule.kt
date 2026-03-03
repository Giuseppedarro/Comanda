package dev.giuseppedarro.comanda.features.tables.di

import dev.giuseppedarro.comanda.core.domain.usecase.LogoutUseCase
import dev.giuseppedarro.comanda.features.tables.data.remote.TableApi
import dev.giuseppedarro.comanda.features.tables.data.repository.TablesRepositoryImpl
import dev.giuseppedarro.comanda.features.tables.domain.repository.TablesRepository
import dev.giuseppedarro.comanda.features.tables.domain.usecase.AddTableUseCase
import dev.giuseppedarro.comanda.features.tables.domain.usecase.GetTablesUseCase
import dev.giuseppedarro.comanda.features.tables.presentation.TableOverviewViewModel
import org.koin.core.qualifier.named
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val tablesModule = module {
    // API
    single {
        TableApi(get(named("authClient")))
    }

    // Repository
    single<TablesRepository> { TablesRepositoryImpl(get()) }

    // Use cases
    factoryOf(::GetTablesUseCase)
    factoryOf(::AddTableUseCase)
    factory { LogoutUseCase(get()) }


    // ViewModel
    viewModel { TableOverviewViewModel(get(), get(), get()) }

}
