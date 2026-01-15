package dev.giuseppedarro.comanda.features.printers.di

import dev.giuseppedarro.comanda.features.printers.data.remote.PrinterApi
import dev.giuseppedarro.comanda.features.printers.data.repository.PrinterRepositoryImpl
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrinterRepository
import dev.giuseppedarro.comanda.features.printers.domain.use_case.CreatePrinterUseCase
import dev.giuseppedarro.comanda.features.printers.domain.use_case.DeletePrinterUseCase
import dev.giuseppedarro.comanda.features.printers.domain.use_case.GetAllPrintersUseCase
import dev.giuseppedarro.comanda.features.printers.domain.use_case.UpdatePrinterUseCase
import dev.giuseppedarro.comanda.features.printers.presentation.PrinterManagementViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val printersModule = module {
    // API
    single {
        PrinterApi(get(named("authClient")))
    }

    // Repository
    single<PrinterRepository> { PrinterRepositoryImpl(get()) }

    // Use cases
    factoryOf(::GetAllPrintersUseCase)
    factoryOf(::CreatePrinterUseCase)
    factoryOf(::UpdatePrinterUseCase)
    factoryOf(::DeletePrinterUseCase)

    // ViewModel
    viewModelOf(::PrinterManagementViewModel)
}