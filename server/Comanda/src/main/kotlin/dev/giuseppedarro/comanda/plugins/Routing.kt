package dev.giuseppedarro.comanda.plugins

import dev.giuseppedarro.comanda.features.auth.domain.usecase.LoginUseCase
import dev.giuseppedarro.comanda.features.auth.domain.usecase.RefreshTokenUseCase
import dev.giuseppedarro.comanda.features.auth.presentation.authRoutes
import dev.giuseppedarro.comanda.features.menu.domain.usecase.GetMenuUseCase
import dev.giuseppedarro.comanda.features.menu.presentation.menuRoutes
import dev.giuseppedarro.comanda.features.orders.domain.usecase.GetOrderByIdUseCase
import dev.giuseppedarro.comanda.features.orders.domain.usecase.GetOrdersForTableUseCase
import dev.giuseppedarro.comanda.features.orders.domain.usecase.GetOrdersUseCase
import dev.giuseppedarro.comanda.features.orders.domain.usecase.SubmitOrderUseCase
import dev.giuseppedarro.comanda.features.orders.presentation.ordersRoutes
import dev.giuseppedarro.comanda.features.payments.domain.usecase.GetBillUseCase
import dev.giuseppedarro.comanda.features.payments.domain.usecase.ProcessPaymentUseCase
import dev.giuseppedarro.comanda.features.payments.presentation.paymentsRoutes
import dev.giuseppedarro.comanda.features.tables.domain.usecase.AddTableUseCase
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
    val addTableUseCase by inject<AddTableUseCase>()
    val submitOrderUseCase by inject<SubmitOrderUseCase>()
    val getOrdersUseCase by inject<GetOrdersUseCase>()
    val getOrdersForTableUseCase by inject<GetOrdersForTableUseCase>()
    val getOrderByIdUseCase by inject<GetOrderByIdUseCase>()
    
    // New injections
    val getMenuUseCase by inject<GetMenuUseCase>()
    val getBillUseCase by inject<GetBillUseCase>()
    val processPaymentUseCase by inject<ProcessPaymentUseCase>()

    routing {
        authRoutes(loginUseCase, refreshTokenUseCase)
        tablesRoutes(getTablesUseCase, addTableUseCase)
        ordersRoutes(submitOrderUseCase, getOrdersUseCase, getOrdersForTableUseCase, getOrderByIdUseCase)
        paymentsRoutes(getBillUseCase, processPaymentUseCase)
        menuRoutes(getMenuUseCase)
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
