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
import dev.giuseppedarro.comanda.features.printers.domain.usecase.CreatePrinterUseCase
import dev.giuseppedarro.comanda.features.printers.domain.usecase.DeletePrinterUseCase
import dev.giuseppedarro.comanda.features.printers.domain.usecase.GetAllPrintersUseCase
import dev.giuseppedarro.comanda.features.printers.domain.usecase.UpdatePrinterUseCase
import dev.giuseppedarro.comanda.features.printers.presentation.printersRoutes
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
    val getMenuUseCase by inject<GetMenuUseCase>()
    val submitOrderUseCase by inject<SubmitOrderUseCase>()
    val getOrdersUseCase by inject<GetOrdersUseCase>()
    val getOrdersForTableUseCase by inject<GetOrdersForTableUseCase>()
    val getOrderByIdUseCase by inject<GetOrderByIdUseCase>()

    val getAllPrintersUseCase by inject<GetAllPrintersUseCase>()
    val createPrinterUseCase by inject<CreatePrinterUseCase>()
    val updatePrinterUseCase by inject<UpdatePrinterUseCase>()
    val deletePrinterUseCase by inject<DeletePrinterUseCase>()

    routing {
        authRoutes(loginUseCase, refreshTokenUseCase)
        tablesRoutes(getTablesUseCase, addTableUseCase)
        menuRoutes(getMenuUseCase)
        ordersRoutes(submitOrderUseCase, getOrdersUseCase, getOrdersForTableUseCase, getOrderByIdUseCase)
        printersRoutes(
            getAllPrintersUseCase,
            createPrinterUseCase,
            updatePrinterUseCase,
            deletePrinterUseCase
        )
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
