package dev.giuseppedarro.comanda.plugins

import dev.giuseppedarro.comanda.features.auth.domain.usecase.LoginUseCase
import dev.giuseppedarro.comanda.features.auth.domain.usecase.RefreshTokenUseCase
import dev.giuseppedarro.comanda.features.auth.presentation.authRoutes
import dev.giuseppedarro.comanda.features.orders.domain.usecase.GetOrderByIdUseCase
import dev.giuseppedarro.comanda.features.orders.domain.usecase.GetOrdersForTableUseCase
import dev.giuseppedarro.comanda.features.orders.domain.usecase.GetOrdersUseCase
import dev.giuseppedarro.comanda.features.orders.domain.usecase.SubmitOrderUseCase
import dev.giuseppedarro.comanda.features.orders.presentation.ordersRoutes
import dev.giuseppedarro.comanda.features.tables.domain.usecase.GetTablesUseCase
import dev.giuseppedarro.comanda.features.tables.presentation.tablesRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val loginUseCase by inject<LoginUseCase>()
    val refreshTokenUseCase by inject<RefreshTokenUseCase>()
    val getTablesUseCase by inject<GetTablesUseCase>()
    val submitOrderUseCase by inject<SubmitOrderUseCase>()
    val getOrdersUseCase by inject<GetOrdersUseCase>()
    val getOrdersForTableUseCase by inject<GetOrdersForTableUseCase>()
    val getOrderByIdUseCase by inject<GetOrderByIdUseCase>()

    routing {
        authRoutes(loginUseCase, refreshTokenUseCase)
        tablesRoutes(getTablesUseCase)
        ordersRoutes(submitOrderUseCase, getOrdersUseCase, getOrdersForTableUseCase, getOrderByIdUseCase)
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
