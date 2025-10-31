package dev.giuseppedarro.comanda.features.orders.di

import dev.giuseppedarro.comanda.features.orders.data.repository.OrdersRepositoryImpl
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrdersRepository
import dev.giuseppedarro.comanda.features.orders.domain.usecase.SubmitOrderUseCase
import org.koin.dsl.module

val ordersModule = module {
    single<OrdersRepository> { OrdersRepositoryImpl() }
    single { SubmitOrderUseCase(get()) }
}
