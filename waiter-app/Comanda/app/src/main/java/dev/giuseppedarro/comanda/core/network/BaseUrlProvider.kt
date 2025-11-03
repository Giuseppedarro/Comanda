package dev.giuseppedarro.comanda.core.network

import java.util.concurrent.atomic.AtomicReference

class BaseUrlProvider(initialUrl: String) {
    private val urlRef = AtomicReference(normalize(initialUrl))

    fun getBaseUrl(): String = urlRef.get()

    fun setBaseUrl(newUrl: String) {
        urlRef.set(normalize(newUrl))
    }

    private fun normalize(url: String): String {
        val trimmed = url.trim()
        // If scheme is missing, keep as-is to let caller decide; Ktor requires a scheme
        return if (trimmed.endsWith("/")) trimmed else "$trimmed/"
    }
}
