package dev.giuseppedarro.comanda.features.menu.di

import androidx.lifecycle.SavedStateHandle
import dev.giuseppedarro.comanda.features.menu.data.remote.MenuApi
import dev.giuseppedarro.comanda.features.menu.data.repository.MenuRepositoryImpl
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository
import dev.giuseppedarro.comanda.features.menu.domain.usecase.AddMenuItemUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.DeleteMenuItemUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.GetMenuUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.UpdateMenuItemUseCase
import dev.giuseppedarro.comanda.features.menu.presentation.CategoryViewModel
import dev.giuseppedarro.comanda.features.menu.presentation.MenuViewModel
import org.koin.core.qualifier.named
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val menuModule = module {
    // API
    single {
        MenuApi(get(named("authClient")))
    }

    // Repository
    single<MenuRepository> { MenuRepositoryImpl(get()) }

    // Use cases
    factoryOf(::GetMenuUseCase)
    factoryOf(::AddMenuItemUseCase)
    factoryOf(::UpdateMenuItemUseCase)
    factoryOf(::DeleteMenuItemUseCase)

    // ViewModels
    viewModel { MenuViewModel(get<GetMenuUseCase>()) }

    viewModel { (savedStateHandle: SavedStateHandle) ->
        CategoryViewModel(
            savedStateHandle,
            get<GetMenuUseCase>(),
            get<AddMenuItemUseCase>(),
            get<UpdateMenuItemUseCase>(),
            get<DeleteMenuItemUseCase>()
        )
    }
}