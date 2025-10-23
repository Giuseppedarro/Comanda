package dev.giuseppedarro.comanda.features.tables.di

import dev.giuseppedarro.comanda.features.tables.presentation.TableOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val tablesModule = module {
    viewModelOf(::TableOverviewViewModel)
}
