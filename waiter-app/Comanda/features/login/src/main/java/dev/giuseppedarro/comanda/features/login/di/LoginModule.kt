package dev.giuseppedarro.comanda.features.login.di

import dev.giuseppedarro.comanda.features.login.data.repository.LoginRepositoryImpl
import dev.giuseppedarro.comanda.features.login.domain.repository.LoginRepository
import dev.giuseppedarro.comanda.features.login.domain.use_case.GetBaseUrlUseCase
import dev.giuseppedarro.comanda.features.login.domain.use_case.LoginUseCase
import dev.giuseppedarro.comanda.features.login.domain.use_case.LogoutUseCase
import dev.giuseppedarro.comanda.features.login.domain.use_case.SetBaseUrlUseCase
import dev.giuseppedarro.comanda.features.login.presentation.LoginViewModel
import io.ktor.client.HttpClient
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
