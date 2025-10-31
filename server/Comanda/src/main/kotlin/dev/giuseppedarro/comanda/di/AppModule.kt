package dev.giuseppedarro.comanda.di

import dev.giuseppedarro.comanda.features.auth.di.authModule
import dev.giuseppedarro.comanda.features.tables.di.tablesModule
import org.koin.dsl.module

val appModule = module {
    includes(authModule, tablesModule)
}
