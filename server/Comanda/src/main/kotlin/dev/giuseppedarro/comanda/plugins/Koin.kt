
package dev.giuseppedarro.comanda.plugins

import dev.giuseppedarro.comanda.di.appModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(appModule)
    }
}
