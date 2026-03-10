package dev.giuseppedarro.comanda.features.tables.domain.model

import dev.giuseppedarro.comanda.core.domain.model.DomainException

sealed class TableException(message: String? = null) : DomainException(message) {
    object TableNotFound : TableException("The requested table was not found.")
}
