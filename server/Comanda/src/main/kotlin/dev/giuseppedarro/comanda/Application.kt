package dev.giuseppedarro.comanda

import dev.giuseppedarro.comanda.di.appModule
import dev.giuseppedarro.comanda.plugins.configureRouting
import dev.giuseppedarro.comanda.plugins.configureSecurity
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Koin) {
        modules(appModule)
    }
    configureSecurity()
    configureRouting()
}
