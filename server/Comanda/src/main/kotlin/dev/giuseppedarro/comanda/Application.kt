
package dev.giuseppedarro.comanda

import dev.giuseppedarro.comanda.plugins.configureKoin
import dev.giuseppedarro.comanda.plugins.configureRouting
import dev.giuseppedarro.comanda.plugins.configureSecurity
import dev.giuseppedarro.comanda.plugins.configureSerialization
import dev.giuseppedarro.comanda.plugins.configureDatabase
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureKoin()
    configureDatabase()
    configureSerialization()
    configureSecurity()
    configureRouting()
}
