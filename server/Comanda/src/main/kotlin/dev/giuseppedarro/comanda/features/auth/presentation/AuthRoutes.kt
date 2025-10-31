package dev.giuseppedarro.comanda.features.auth.presentation

import dev.giuseppedarro.comanda.features.auth.data.model.LoginRequest
import dev.giuseppedarro.comanda.features.auth.data.model.RefreshTokenRequest
import dev.giuseppedarro.comanda.features.auth.domain.usecase.LoginUseCase
import dev.giuseppedarro.comanda.features.auth.domain.usecase.RefreshTokenUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes(loginUseCase: LoginUseCase, refreshTokenUseCase: RefreshTokenUseCase) {
    route("/auth") {
        post("/login") {
            println("get auth/login is called")
            val request = call.receive<LoginRequest>()
            loginUseCase(request)
                .onSuccess {
                    call.respond(HttpStatusCode.OK, it)
                }
                .onFailure {
                    call.respond(HttpStatusCode.Unauthorized, it.message ?: "")
                }
        }

        post("/refresh") {
            val request = call.receive<RefreshTokenRequest>()
            refreshTokenUseCase(request)
                .onSuccess {
                    call.respond(HttpStatusCode.OK, it)
                }
                .onFailure {
                    call.respond(HttpStatusCode.Unauthorized, it.message ?: "")
                }
        }
    }
}
