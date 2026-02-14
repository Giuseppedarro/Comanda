package dev.giuseppedarro.comanda.features.users.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val employeeId: String,
    val name: String,
    val role: String
    // Password is intentionally excluded from the domain model to prevent accidental exposure
)
