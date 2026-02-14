package dev.giuseppedarro.comanda.features.users.di

import dev.giuseppedarro.comanda.features.users.data.repository.UsersRepositoryImpl
import dev.giuseppedarro.comanda.features.users.domain.repository.UsersRepository
import dev.giuseppedarro.comanda.features.users.domain.usecase.CreateUserUseCase
import dev.giuseppedarro.comanda.features.users.domain.usecase.DeleteUserUseCase
import dev.giuseppedarro.comanda.features.users.domain.usecase.GetUsersUseCase
import dev.giuseppedarro.comanda.features.users.domain.usecase.UpdateUserUseCase
import org.koin.dsl.module

val usersModule = module {
    single<UsersRepository> { UsersRepositoryImpl() }
    single { CreateUserUseCase(get()) }
    single { GetUsersUseCase(get()) }
    single { UpdateUserUseCase(get()) }
    single { DeleteUserUseCase(get()) }
}
