package dev.giuseppedarro.comanda.core.di

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import dev.giuseppedarro.comanda.core.data.CryptoManager
import dev.giuseppedarro.comanda.core.data.TokenRepositoryImpl
import dev.giuseppedarro.comanda.core.domain.TokenRepository
import dev.giuseppedarro.comanda.core.network.BaseUrlProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
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

    // Ktor HttpClient
    single {
        HttpClient(CIO) {
            // Logging
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("KTOR_CLIENT", message)
                    }
                }
            }

            // JSON Serialization
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }

            // Default Request
            val baseUrlProvider: BaseUrlProvider = get()
            defaultRequest {
                // Read current base URL dynamically for each request
                url(baseUrlProvider.getBaseUrl())
            }
        }
    }
}
