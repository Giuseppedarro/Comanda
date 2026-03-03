package dev.giuseppedarro.comanda.features.login.domain.usecase

import dev.giuseppedarro.comanda.core.network.BaseUrlProvider

class GetBaseUrlUseCase(private val baseUrlProvider: BaseUrlProvider) {
    operator fun invoke(): String = baseUrlProvider.getBaseUrl()
}