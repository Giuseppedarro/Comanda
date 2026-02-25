package dev.giuseppedarro.comanda.core.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import dev.giuseppedarro.comanda.core.data.CryptoManager
import dev.giuseppedarro.comanda.core.data.TokenRepositoryImpl
import dev.giuseppedarro.comanda.core.data.repository.ThemeRepositoryImpl
import dev.giuseppedarro.comanda.core.domain.TokenRepository
import dev.giuseppedarro.comanda.core.domain.repository.ThemeRepository
import dev.giuseppedarro.comanda.core.domain.use_case.GetThemePreferencesUseCase
import dev.giuseppedarro.comanda.core.domain.use_case.SaveThemePreferencesUseCase
import dev.giuseppedarro.comanda.core.network.AndroidKtorLogger
import dev.giuseppedarro.comanda.core.network.BaseUrlProvider
import dev.giuseppedarro.comanda.core.network.HttpClientConfig
import dev.giuseppedarro.comanda.core.network.KtorLogger
import dev.giuseppedarro.comanda.core.network.createBasicHttpClient
import dev.giuseppedarro.comanda.core.network.createHttpClient
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coreModule = module {
    // Encrypted token storage
    single<CryptoManager> { CryptoManager() }
    single<DataStore<Preferences>>(named("tokenDataStore")) {
        val context = androidContext()
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("tokens.preferences_pb") }
        )
    }
    single<TokenRepository> { TokenRepositoryImpl(get(named("tokenDataStore")), get()) }

    // Theme preferences storage
    single<DataStore<Preferences>>(named("themeDataStore")) {
        val context = androidContext()
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("theme.preferences_pb") }
        )
    }
    single<ThemeRepository> { ThemeRepositoryImpl(get(named("themeDataStore"))) }
    single { GetThemePreferencesUseCase(get()) }
    single { SaveThemePreferencesUseCase(get()) }

    // Base URL provider for dynamic configuration
    single { BaseUrlProvider("http://10.0.2.2:8080/") }

    // Networking configuration
    single<KtorLogger> { AndroidKtorLogger() }
    single { HttpClientConfig() }

    // Basic HTTP client (without Auth) - for login and token refresh
    single<HttpClient>(named("basicClient")) {
        createBasicHttpClient(
            baseUrlProvider = get(),
            logger = get(),
            config = get()
        )
    }

    // Main HTTP client with Auth plugin - for all authenticated API calls
    single<HttpClient>(named("authClient")) {
        createHttpClient(
            baseUrlProvider = get(),
            tokenRepository = get(),
            logger = get(),
            config = get(),
            basicClient = get(named("basicClient"))
        )
    }
}
