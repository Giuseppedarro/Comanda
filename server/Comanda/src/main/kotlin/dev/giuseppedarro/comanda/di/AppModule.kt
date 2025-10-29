
package dev.giuseppedarro.comanda.di

import org.koin.dsl.module

val appModule = module {
    includes(authModule)
}
