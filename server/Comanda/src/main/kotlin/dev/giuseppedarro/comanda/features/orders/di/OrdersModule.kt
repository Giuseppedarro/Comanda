package dev.giuseppedarro.comanda.features.orders.di

import dev.giuseppedarro.comanda.features.orders.data.repository.OrdersRepositoryImpl
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrdersRepository
import dev.giuseppedarro.comanda.features.orders.domain.usecase.GetOrderByIdUseCase
import dev.giuseppedarro.comanda.features.orders.domain.usecase.GetOrdersForTableUseCase
import dev.giuseppedarro.comanda.features.orders.domain.usecase.GetOrdersUseCase
import dev.giuseppedarro.comanda.features.orders.domain.usecase.SubmitOrderUseCase
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrintersRepository
import dev.giuseppedarro.comanda.features.printers.domain.usecase.SendTicketToPrinterUseCase
import org.koin.dsl.module

val ordersModule = module {
    single<OrdersRepository> {
        OrdersRepositoryImpl(
            sendTicketToPrinterUseCase = get(),
            printersRepository = get()
        )
    }
    single { SubmitOrderUseCase(get(), get()) }
    single { GetOrdersUseCase(get()) }
    single { GetOrdersForTableUseCase(get()) }
    single { GetOrderByIdUseCase(get()) }
}
