package dev.giuseppedarro.comanda

import android.app.Application
import dev.giuseppedarro.comanda.core.di.coreModule
import dev.giuseppedarro.comanda.features.login.di.loginModule
import dev.giuseppedarro.comanda.features.orders.di.ordersModule
import dev.giuseppedarro.comanda.features.tables.di.tablesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ComandaApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ComandaApp)
            modules(loginModule, tablesModule, ordersModule, coreModule)
        }
    }
}
