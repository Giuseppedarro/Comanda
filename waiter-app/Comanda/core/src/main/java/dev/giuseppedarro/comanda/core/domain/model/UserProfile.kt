package dev.giuseppedarro.comanda.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: Int,
    val employeeId: String,
    val name: String,
    val role: String
)
