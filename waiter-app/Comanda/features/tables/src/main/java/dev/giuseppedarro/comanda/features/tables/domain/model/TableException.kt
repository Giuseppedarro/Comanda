package dev.giuseppedarro.comanda.features.tables.domain.model

import dev.giuseppedarro.comanda.core.domain.model.DomainException

sealed class TableException(message: String? = null) : DomainException(message) {
    object TableNotFound : TableException("The requested table was not found.")
    object TableAlreadyExists : TableException("The table number already exists.")
    object InvalidTableNumber : TableException("Table number must be a positive integer.")
}
