package dev.giuseppedarro.comanda.core.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import dev.giuseppedarro.comanda.core.data.CryptoManager
import dev.giuseppedarro.comanda.core.data.TokenRepositoryImpl
import dev.giuseppedarro.comanda.core.domain.TokenRepository
import dev.giuseppedarro.comanda.core.domain.use_case.RefreshTokenUseCase
import dev.giuseppedarro.comanda.core.network.BaseUrlProvider
import dev.giuseppedarro.comanda.core.network.createBasicHttpClient
import dev.giuseppedarro.comanda.core.network.createHttpClient
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coreModule = module {
    // Encrypted token storage
    single<CryptoManager> { CryptoManager() }
    single<DataStore<Preferences>> {
        val context = androidContext()
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("tokens.preferences_pb") }
        )
    }
    single<TokenRepository> { TokenRepositoryImpl(get(), get()) }

    // Base URL provider for dynamic configuration
    single { BaseUrlProvider("http://10.0.2.2:8080/") }

    // Basic HTTP client (without Auth) - for login and token refresh
    // This prevents circular dependencies
    single<HttpClient>(named("basicClient")) {
        createBasicHttpClient(get())
    }

    // Refresh token use case - uses basic client to avoid circular dependency
    factory<RefreshTokenUseCase> {
        val basicClient = get<HttpClient>(named("basicClient"))
        RefreshTokenUseCase(basicClient, get())
    }

    // Main HTTP client with Auth plugin - for all authenticated API calls
    single<HttpClient>(named("authClient")) {
        createHttpClient(get(), get(), get())
    }
}
