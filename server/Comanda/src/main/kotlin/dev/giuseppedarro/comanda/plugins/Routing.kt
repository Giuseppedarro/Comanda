
package dev.giuseppedarro.comanda.plugins

import dev.giuseppedarro.comanda.auth.domain.usecase.LoginUseCase
import dev.giuseppedarro.comanda.auth.domain.usecase.RefreshTokenUseCase
import dev.giuseppedarro.comanda.auth.presentation.authRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val loginUseCase by inject<LoginUseCase>()
    val refreshTokenUseCase by inject<RefreshTokenUseCase>()
    routing {
        authRoutes(loginUseCase, refreshTokenUseCase)
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
