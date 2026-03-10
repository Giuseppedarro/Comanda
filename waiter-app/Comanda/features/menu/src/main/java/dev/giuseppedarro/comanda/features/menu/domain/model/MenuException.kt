package dev.giuseppedarro.comanda.features.menu.domain.model

import dev.giuseppedarro.comanda.core.domain.model.DomainException

sealed class MenuException(message: String? = null) : DomainException(message) {
    object CategoryNotEmpty : MenuException("Cannot delete a category that contains items.")
    object DuplicateName : MenuException("An item or category with this name already exists.")
    object EmptyName : MenuException("Name cannot be empty.")
    object InvalidPrice : MenuException("Price must be a valid positive number.")
}
