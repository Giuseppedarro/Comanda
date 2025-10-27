package dev.giuseppedarro.comanda.features.login.di

import dev.giuseppedarro.comanda.features.login.data.repository.LoginRepositoryImpl
import dev.giuseppedarro.comanda.features.login.domain.repository.LoginRepository
import dev.giuseppedarro.comanda.features.login.domain.use_case.LoginUseCase
import dev.giuseppedarro.comanda.features.login.presentation.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    single<LoginRepository> { LoginRepositoryImpl(get()) } // Pass HttpClient to the repository
    single { LoginUseCase(get()) }
    viewModel { LoginViewModel(get()) }
}
