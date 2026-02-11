package dev.giuseppedarro.comanda.features.users.di

import dev.giuseppedarro.comanda.features.users.data.repository.UsersRepositoryImpl
import dev.giuseppedarro.comanda.features.users.domain.repository.UsersRepository
import dev.giuseppedarro.comanda.features.users.domain.usecase.CreateUserUseCase
import org.koin.dsl.module

val usersModule = module {
    single<UsersRepository> { UsersRepositoryImpl() }
    single { CreateUserUseCase(get()) }
}
