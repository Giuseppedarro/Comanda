package dev.giuseppedarro.comanda.features.menu.di

import dev.giuseppedarro.comanda.features.menu.data.repository.MenuRepositoryImpl
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository
import dev.giuseppedarro.comanda.features.menu.domain.usecase.*
import org.koin.dsl.module

val menuModule = module {
    single<MenuRepository> { MenuRepositoryImpl() }
    single { GetMenuUseCase(get()) }
    single { UpdateMenuUseCase(get()) }
    
    // Category Use Cases
    single { AddCategoryUseCase(get()) }
    single { UpdateCategoryUseCase(get()) }
    single { DeleteCategoryUseCase(get()) }
    single { GetCategoryUseCase(get()) }
    
    // Item Use Cases
    single { AddItemUseCase(get()) }
    single { UpdateItemUseCase(get()) }
    single { DeleteItemUseCase(get()) }
    single { GetItemUseCase(get()) }
}