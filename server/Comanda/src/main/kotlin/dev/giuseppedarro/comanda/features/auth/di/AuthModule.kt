package dev.giuseppedarro.comanda.features.auth.di

import dev.giuseppedarro.comanda.features.auth.data.datasource.AuthDataSource
import dev.giuseppedarro.comanda.features.auth.data.repository.AuthRepositoryImpl
import dev.giuseppedarro.comanda.features.auth.domain.repository.AuthRepository
import dev.giuseppedarro.comanda.features.auth.domain.usecase.LoginUseCase
import dev.giuseppedarro.comanda.features.auth.domain.usecase.RefreshTokenUseCase
import org.koin.dsl.module

val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single { AuthDataSource() }
    single { LoginUseCase(get()) }
    single { RefreshTokenUseCase(get()) }
}
