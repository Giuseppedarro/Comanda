package dev.giuseppedarro.comanda.features.orders.di

import androidx.lifecycle.SavedStateHandle
import dev.giuseppedarro.comanda.features.orders.data.remote.OrderApi
import dev.giuseppedarro.comanda.features.orders.data.repository.OrderRepositoryImpl
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrderRepository
import dev.giuseppedarro.comanda.features.orders.domain.use_case.GetMenuUseCase
import dev.giuseppedarro.comanda.features.orders.domain.use_case.GetOrdersForTableUseCase
import dev.giuseppedarro.comanda.features.orders.domain.use_case.SubmitOrderUseCase
import dev.giuseppedarro.comanda.features.orders.presentation.MenuOrderViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val ordersModule = module {
    // API
    single { OrderApi(get()) }

    // Repository
    single<OrderRepository> { OrderRepositoryImpl(get(), get()) }

    // Use cases
    factoryOf(::GetMenuUseCase)
    factoryOf(::GetOrdersForTableUseCase)
    factoryOf(::SubmitOrderUseCase)

    // ViewModel
    viewModel { (savedStateHandle: SavedStateHandle) ->
        MenuOrderViewModel(
            savedStateHandle,
            get<GetMenuUseCase>(),
            get<GetOrdersForTableUseCase>(),
            get<SubmitOrderUseCase>()
        )
    }
}
