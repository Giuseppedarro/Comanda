package dev.giuseppedarro.comanda.di

import dev.giuseppedarro.comanda.features.auth.di.authModule
import dev.giuseppedarro.comanda.features.menu.di.menuModule
import dev.giuseppedarro.comanda.features.orders.di.ordersModule
import dev.giuseppedarro.comanda.features.payments.di.paymentsModule
import dev.giuseppedarro.comanda.features.tables.di.tablesModule
import io.ktor.server.config.*
import org.koin.dsl.module

fun appModule(config: ApplicationConfig) = module {
    includes(authModule(config), tablesModule, ordersModule, menuModule, paymentsModule)
}
