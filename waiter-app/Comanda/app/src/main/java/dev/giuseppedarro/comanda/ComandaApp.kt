package dev.giuseppedarro.comanda

import android.app.Application
import dev.giuseppedarro.comanda.core.di.dataModule
import dev.giuseppedarro.comanda.features.login.di.loginModule
import dev.giuseppedarro.comanda.features.orders.di.ordersModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ComandaApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ComandaApp)
            modules(loginModule, ordersModule, dataModule)
        }
    }
}
