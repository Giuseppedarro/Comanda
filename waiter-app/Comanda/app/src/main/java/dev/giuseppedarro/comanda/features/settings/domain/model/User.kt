package dev.giuseppedarro.comanda.features.settings.domain.model

data class User(
    val id: String,
    val employeeId: String,
    val name: String,
    val role: String
)