package dev.giuseppedarro.comanda.features.login.di

import dev.giuseppedarro.comanda.features.login.presentation.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val loginModule = module {
    viewModelOf(::LoginViewModel)
}
