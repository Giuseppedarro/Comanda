package dev.giuseppedarro.comanda.features.settings.domain.model

import dev.giuseppedarro.comanda.core.domain.model.DomainException

sealed class UserException(message: String? = null) : DomainException(message) {
    object DuplicateEmployeeId : UserException("A user with this employee ID already exists.")
    object UserNotFound : UserException("The requested user was not found.")
    object InvalidUserData : UserException("Provided user data is invalid.")
    object InvalidUserId : UserException("The user ID is invalid.")
}

