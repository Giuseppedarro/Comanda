package dev.giuseppedarro.comanda.features.orders.di

import dev.giuseppedarro.comanda.features.orders.data.remote.OrderApi
import dev.giuseppedarro.comanda.features.orders.data.repository.OrderRepositoryImpl
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrderRepository
import dev.giuseppedarro.comanda.features.orders.domain.use_case.GetMenuUseCase
import dev.giuseppedarro.comanda.features.orders.domain.use_case.GetOrdersForTableUseCase
import dev.giuseppedarro.comanda.features.orders.domain.use_case.SubmitOrderUseCase
import dev.giuseppedarro.comanda.features.orders.presentation.MenuOrderViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ordersModule = module {
    factoryOf(::OrderApi)
    factoryOf(::GetMenuUseCase) // Added this back
    factoryOf(::GetOrdersForTableUseCase)
    factoryOf(::SubmitOrderUseCase)
    factory { OrderRepositoryImpl(get(), get()) } bind OrderRepository::class

    viewModelOf(::MenuOrderViewModel)
}
