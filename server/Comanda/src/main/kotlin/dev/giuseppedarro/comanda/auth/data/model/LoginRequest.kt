
package dev.giuseppedarro.comanda.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val employeeId: String, val password: String)
