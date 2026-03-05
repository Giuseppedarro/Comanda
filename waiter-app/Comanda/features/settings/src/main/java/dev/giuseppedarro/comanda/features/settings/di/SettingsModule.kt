package dev.giuseppedarro.comanda.features.settings.di

import dev.giuseppedarro.comanda.core.network.UserApi
import dev.giuseppedarro.comanda.features.settings.data.repository.UserRepositoryImpl
import dev.giuseppedarro.comanda.features.settings.domain.repository.UserRepository
import dev.giuseppedarro.comanda.features.settings.domain.usecase.CreateUserUseCase
import dev.giuseppedarro.comanda.features.settings.domain.usecase.DeleteUserUseCase
import dev.giuseppedarro.comanda.features.settings.domain.usecase.GetUsersUseCase
import dev.giuseppedarro.comanda.features.settings.domain.usecase.UpdateUserUseCase
import dev.giuseppedarro.comanda.features.settings.presentation.LanguageSettingsViewModel
import dev.giuseppedarro.comanda.features.settings.presentation.ManageUsersViewModel
import dev.giuseppedarro.comanda.features.settings.presentation.ThemeSettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    single<UserRepository> { UserRepositoryImpl(get<UserApi>()) }
    factory { CreateUserUseCase(get()) }
    factory { GetUsersUseCase(get()) }
    factory { UpdateUserUseCase(get()) }
    factory { DeleteUserUseCase(get()) }
    viewModel { ManageUsersViewModel(get(), get(), get(), get()) }
    viewModel { ThemeSettingsViewModel(get(), get()) }
    viewModel { LanguageSettingsViewModel(get(), get()) }
}
