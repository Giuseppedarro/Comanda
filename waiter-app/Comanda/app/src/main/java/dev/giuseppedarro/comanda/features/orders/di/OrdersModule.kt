package dev.giuseppedarro.comanda.features.orders.di

import androidx.lifecycle.SavedStateHandle
import dev.giuseppedarro.comanda.features.orders.presentation.MenuOrderViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val ordersModule = module {
    viewModel { (savedStateHandle: SavedStateHandle) -> MenuOrderViewModel(savedStateHandle) }
}
