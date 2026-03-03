package dev.giuseppedarro.comanda.features.login.di

import dev.giuseppedarro.comanda.features.login.data.repository.LoginRepositoryImpl
import dev.giuseppedarro.comanda.features.login.domain.repository.LoginRepository
import dev.giuseppedarro.comanda.features.login.domain.usecase.GetBaseUrlUseCase
import dev.giuseppedarro.comanda.features.login.domain.usecase.LoginUseCase
import dev.giuseppedarro.comanda.core.domain.usecase.LogoutUseCase
import dev.giuseppedarro.comanda.features.login.domain.usecase.SetBaseUrlUseCase
import dev.giuseppedarro.comanda.features.login.presentation.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val loginModule = module {
    single<LoginRepository> {
        LoginRepositoryImpl(
            client = get(named("basicClient")),
            tokenStorage = get()
        )
    }
    single { LoginUseCase(get()) }
    single { GetBaseUrlUseCase(get()) }
    single { SetBaseUrlUseCase(get()) }
    single { LogoutUseCase(get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
}
