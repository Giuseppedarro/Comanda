package dev.giuseppedarro.comanda.features.menu.di

import dev.giuseppedarro.comanda.features.menu.data.repository.MenuRepositoryImpl
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository
import dev.giuseppedarro.comanda.features.menu.domain.usecase.GetMenuUseCase
import org.koin.dsl.module

val menuModule = module {
    single<MenuRepository> { MenuRepositoryImpl() }
    single { GetMenuUseCase(get()) }
}