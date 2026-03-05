package dev.giuseppedarro.comanda.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val employeeId: String,
    val name: String,
    val password: String,
    val role: String
)
