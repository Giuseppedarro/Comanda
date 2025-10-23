package dev.giuseppedarro.comanda.features.orders.di

import dev.giuseppedarro.comanda.features.orders.presentation.MenuOrderViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val ordersModule = module {
    viewModelOf(::MenuOrderViewModel)
}
