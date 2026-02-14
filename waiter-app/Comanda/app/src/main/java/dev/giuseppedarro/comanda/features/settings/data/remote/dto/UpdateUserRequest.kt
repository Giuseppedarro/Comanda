package dev.giuseppedarro.comanda.features.settings.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequest(
    val name: String? = null,
    val role: String? = null,
    val password: String? = null,
    val employeeId: String? = null
)
