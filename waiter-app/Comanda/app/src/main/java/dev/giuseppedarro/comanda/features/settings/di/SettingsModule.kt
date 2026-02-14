package dev.giuseppedarro.comanda.features.settings.di

import dev.giuseppedarro.comanda.features.settings.data.remote.UserApi
import dev.giuseppedarro.comanda.features.settings.data.repository.UserRepositoryImpl
import dev.giuseppedarro.comanda.features.settings.domain.repository.UserRepository
import dev.giuseppedarro.comanda.features.settings.domain.use_case.CreateUserUseCase
import dev.giuseppedarro.comanda.features.settings.domain.use_case.DeleteUserUseCase
import dev.giuseppedarro.comanda.features.settings.domain.use_case.GetUsersUseCase
import dev.giuseppedarro.comanda.features.settings.domain.use_case.UpdateUserUseCase
import dev.giuseppedarro.comanda.features.settings.presentation.ManageUsersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val settingsModule = module {
    single { UserApi(get(named("authClient"))) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    factory { CreateUserUseCase(get()) }
    factory { GetUsersUseCase(get()) }
    factory { UpdateUserUseCase(get()) }
    factory { DeleteUserUseCase(get()) }
    viewModel { ManageUsersViewModel(get(), get(), get(), get()) }
}
