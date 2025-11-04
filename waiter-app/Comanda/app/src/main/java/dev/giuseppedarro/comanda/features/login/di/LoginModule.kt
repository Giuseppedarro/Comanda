package dev.giuseppedarro.comanda.features.login.di

import dev.giuseppedarro.comanda.features.login.data.repository.LoginRepositoryImpl
import dev.giuseppedarro.comanda.features.login.domain.repository.LoginRepository
import dev.giuseppedarro.comanda.features.login.domain.use_case.GetBaseUrlUseCase
import dev.giuseppedarro.comanda.features.login.domain.use_case.LoginUseCase
import dev.giuseppedarro.comanda.features.login.domain.use_case.SetBaseUrlUseCase
import dev.giuseppedarro.comanda.features.login.presentation.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    single<LoginRepository> { LoginRepositoryImpl(get(), get()) } // HttpClient + TokenStorage
    single { LoginUseCase(get()) }
    single { GetBaseUrlUseCase(get()) }
    single { SetBaseUrlUseCase(get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
}
