package dev.giuseppedarro.comanda.features.settings.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Int,
    val employeeId: String,
    val name: String,
    val role: String
)
