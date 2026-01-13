package dev.giuseppedarro.comanda.features.printers.di

import dev.giuseppedarro.comanda.features.printers.data.repository.PrintersRepositoryImpl
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrintersRepository
import dev.giuseppedarro.comanda.features.printers.domain.usecase.CreatePrinterUseCase
import dev.giuseppedarro.comanda.features.printers.domain.usecase.DeletePrinterUseCase
import dev.giuseppedarro.comanda.features.printers.domain.usecase.GetAllPrintersUseCase
import dev.giuseppedarro.comanda.features.printers.domain.usecase.SendTicketToPrinterUseCase
import dev.giuseppedarro.comanda.features.printers.domain.usecase.UpdatePrinterUseCase
import org.koin.dsl.module

val printersModule = module {
    single<PrintersRepository> { PrintersRepositoryImpl() }
    single { GetAllPrintersUseCase(get()) }
    single { CreatePrinterUseCase(get()) }
    single { UpdatePrinterUseCase(get()) }
    single { DeletePrinterUseCase(get()) }
    single { SendTicketToPrinterUseCase(get()) }
}