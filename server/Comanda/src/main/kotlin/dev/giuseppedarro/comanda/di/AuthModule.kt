
package dev.giuseppedarro.comanda.di

import dev.giuseppedarro.comanda.auth.data.datasource.AuthDataSource
import dev.giuseppedarro.comanda.auth.data.repository.AuthRepositoryImpl
import dev.giuseppedarro.comanda.auth.domain.repository.AuthRepository
import dev.giuseppedarro.comanda.auth.domain.usecase.LoginUseCase
import dev.giuseppedarro.comanda.auth.domain.usecase.RefreshTokenUseCase
import org.koin.dsl.module

val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single { AuthDataSource() }
    single { LoginUseCase(get()) }
    single { RefreshTokenUseCase(get()) }
}
