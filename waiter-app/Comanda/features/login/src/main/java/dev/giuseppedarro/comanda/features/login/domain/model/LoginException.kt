package dev.giuseppedarro.comanda.features.login.domain.model

import dev.giuseppedarro.comanda.core.domain.model.DomainException

sealed class LoginException(message: String? = null) : DomainException(message) {
    object InvalidCredentials : LoginException()
    object EmptyCredentials : LoginException()
    data class UnknownError(val error: String?) : LoginException(error)
}
