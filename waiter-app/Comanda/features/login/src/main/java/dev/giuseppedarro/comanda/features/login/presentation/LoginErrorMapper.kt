package dev.giuseppedarro.comanda.features.login.presentation

import dev.giuseppedarro.comanda.core.domain.model.DomainException
import dev.giuseppedarro.comanda.core.presentation.UiText
import dev.giuseppedarro.comanda.core.presentation.toUiText
import dev.giuseppedarro.comanda.features.login.R
import dev.giuseppedarro.comanda.features.login.domain.model.LoginException

fun Throwable.toLoginUiText(): UiText {
    return when (this) {
        is LoginException.InvalidCredentials -> UiText.StringResource(R.string.error_invalid_credentials)
        is LoginException.EmptyCredentials -> UiText.StringResource(R.string.error_empty_credentials)
        is DomainException -> this.toUiText()
        else -> UiText.DynamicString(this.message ?: "Unknown error")
    }
}
