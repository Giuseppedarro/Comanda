package dev.giuseppedarro.comanda.features.auth.di

import dev.giuseppedarro.comanda.features.auth.data.datasource.AuthDataSource
import dev.giuseppedarro.comanda.features.auth.data.repository.AuthRepositoryImpl
import dev.giuseppedarro.comanda.features.auth.domain.repository.AuthRepository
import dev.giuseppedarro.comanda.features.auth.domain.usecase.LoginUseCase
import dev.giuseppedarro.comanda.features.auth.domain.usecase.RefreshTokenUseCase
import io.ktor.server.config.*
import org.koin.dsl.module

fun authModule(config: ApplicationConfig) = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single { 
        AuthDataSource(
            jwtAudience = config.property("jwt.audience").getString(),
            jwtDomain = config.property("jwt.issuer").getString(),
            jwtSecret = config.property("jwt.secret").getString()
        ) 
    }
    single { LoginUseCase(get()) }
    single { RefreshTokenUseCase(get()) }
}
