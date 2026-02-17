package dev.giuseppedarro.comanda.features.login.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val employeeId: String,
    val password: String
)
