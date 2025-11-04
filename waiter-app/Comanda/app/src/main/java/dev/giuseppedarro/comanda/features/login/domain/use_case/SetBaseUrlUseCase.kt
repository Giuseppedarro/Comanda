package dev.giuseppedarro.comanda.features.login.domain.use_case

import dev.giuseppedarro.comanda.core.network.BaseUrlProvider

class SetBaseUrlUseCase(private val baseUrlProvider: BaseUrlProvider) {
    operator fun invoke(newUrl: String) {
        baseUrlProvider.setBaseUrl(sanitize(newUrl))
    }

    private fun sanitize(input: String): String {
        val t = input.trim()
        val withScheme = if (t.startsWith("http://") || t.startsWith("https://")) t else "http://$t"
        return if (withScheme.endsWith("/")) withScheme else "$withScheme/"
    }
}