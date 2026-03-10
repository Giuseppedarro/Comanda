package dev.giuseppedarro.comanda.core.presentation

import dev.giuseppedarro.comanda.core.R
import dev.giuseppedarro.comanda.core.domain.model.DomainException

fun DomainException.toUiText(): UiText {
    return when (this) {
        is DomainException.NetworkException -> UiText.StringResource(R.string.error_network)
        is DomainException.ServerException -> UiText.StringResource(R.string.error_server)
        is DomainException.UnauthorizedException -> UiText.StringResource(R.string.error_unauthorized)
        is DomainException.NotFoundException -> UiText.StringResource(R.string.error_not_found)
        is DomainException.UnknownException -> {
            this.originalMessage?.let { UiText.DynamicString(it) }
                ?: UiText.StringResource(R.string.error_unknown)
        }
        else -> UiText.StringResource(R.string.error_unknown)
    }
}
