package dev.giuseppedarro.comanda.features.payments.di

import dev.giuseppedarro.comanda.features.payments.domain.usecase.GetBillUseCase
import dev.giuseppedarro.comanda.features.payments.domain.usecase.ProcessPaymentUseCase
import org.koin.dsl.module

val paymentsModule = module {
    single { GetBillUseCase(get(), get()) }
    single { ProcessPaymentUseCase() }
}
