package dev.giuseppedarro.comanda.core.di

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import dev.giuseppedarro.comanda.core.data.CryptoManager
import dev.giuseppedarro.comanda.core.data.ServerAddressRepositoryImpl
import dev.giuseppedarro.comanda.core.data.TokenRepositoryImpl
import dev.giuseppedarro.comanda.core.domain.ServerAddressRepository
import dev.giuseppedarro.comanda.core.domain.TokenRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.url
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val coreModule = module {
    // Encrypted token storage & Server Address
    single<CryptoManager> { CryptoManager() }
    single<DataStore<Preferences>> {
        val context = get<Context>()
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("settings.preferences_pb") }
        )
    }
    single<TokenRepository> { TokenRepositoryImpl(get(), get()) }
    single<ServerAddressRepository> { ServerAddressRepositoryImpl(get()) }

    // Ktor HttpClient
    single {
        val serverAddressRepository = get<ServerAddressRepository>()

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

            // Dynamic Base URL Plugin
            install(createClientPlugin("DynamicBaseUrl") {
                onRequest { request, _ ->
                    val originalUrl = request.url.encodedPath
                    val address = serverAddressRepository.getAddress().first()
                    request.url("http://$address$originalUrl")
                }
            })
        }
    }
}
