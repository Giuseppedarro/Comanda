package dev.giuseppedarro.comanda.features.tables.di

import dev.giuseppedarro.comanda.core.domain.TokenRepository
import dev.giuseppedarro.comanda.core.domain.use_case.LogoutUseCase
import dev.giuseppedarro.comanda.features.tables.data.remote.TableApi
import dev.giuseppedarro.comanda.features.tables.data.repository.TablesRepositoryImpl
import dev.giuseppedarro.comanda.features.tables.domain.repository.TablesRepository
import dev.giuseppedarro.comanda.features.tables.domain.use_case.AddTableUseCase
import dev.giuseppedarro.comanda.features.tables.domain.use_case.GetTablesUseCase
import dev.giuseppedarro.comanda.features.tables.presentation.TableOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
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
