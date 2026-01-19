package dev.giuseppedarro.comanda.features.menu.di

import dev.giuseppedarro.comanda.features.menu.data.remote.MenuApi
import dev.giuseppedarro.comanda.features.menu.data.repository.MenuRepositoryImpl
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository
import dev.giuseppedarro.comanda.features.menu.domain.usecase.AddMenuItemUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.CreateCategoryUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.DeleteCategoryUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.DeleteMenuItemUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.GetMenuUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.UpdateCategoryUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.UpdateMenuItemUseCase
import dev.giuseppedarro.comanda.features.menu.presentation.MenuViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val menuModule = module {
    single { MenuApi(get()) }
    single<MenuRepository> { MenuRepositoryImpl(get()) }

    factoryOf(::GetMenuUseCase)
    factoryOf(::CreateCategoryUseCase)
    factoryOf(::UpdateCategoryUseCase)
    factoryOf(::DeleteCategoryUseCase)
    factoryOf(::AddMenuItemUseCase)
    factoryOf(::UpdateMenuItemUseCase)
    factoryOf(::DeleteMenuItemUseCase)

    viewModel { MenuViewModel(get(), get(), get(), get(), get(), get(), get()) }
}