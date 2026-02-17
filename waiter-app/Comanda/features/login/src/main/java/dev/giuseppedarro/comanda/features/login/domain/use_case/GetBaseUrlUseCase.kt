package dev.giuseppedarro.comanda.features.login.domain.use_case

import dev.giuseppedarro.comanda.core.network.BaseUrlProvider

class GetBaseUrlUseCase(private val baseUrlProvider: BaseUrlProvider) {
    operator fun invoke(): String = baseUrlProvider.getBaseUrl()
}