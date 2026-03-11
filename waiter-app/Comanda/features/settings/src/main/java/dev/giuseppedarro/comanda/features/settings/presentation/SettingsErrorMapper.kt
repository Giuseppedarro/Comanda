package dev.giuseppedarro.comanda.features.settings.presentation

import dev.giuseppedarro.comanda.core.domain.model.DomainException
import dev.giuseppedarro.comanda.core.presentation.UiText
import dev.giuseppedarro.comanda.core.presentation.toUiText
import dev.giuseppedarro.comanda.features.settings.R
import dev.giuseppedarro.comanda.features.settings.domain.model.UserException

fun Throwable.toSettingsUiText(): UiText {
    return when (this) {
        is UserException.DuplicateEmployeeId -> UiText.StringResource(R.string.error_user_duplicate_employee_id)
        is UserException.UserNotFound -> UiText.StringResource(R.string.error_user_not_found)
        is UserException.InvalidUserData -> UiText.StringResource(R.string.error_user_invalid_data)
        is UserException.InvalidUserId -> UiText.StringResource(R.string.error_user_invalid_id)
        is DomainException -> this.toUiText()
        else -> DomainException.UnknownException(message).toUiText()
    }
}

