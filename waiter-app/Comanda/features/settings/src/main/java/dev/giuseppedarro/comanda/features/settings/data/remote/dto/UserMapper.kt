package dev.giuseppedarro.comanda.features.settings.data.remote.dto

import dev.giuseppedarro.comanda.features.settings.domain.model.User

fun UserResponse.toDomain(): User = User(
    id = id.toString(),
    employeeId = employeeId,
    name = name,
    role = role
)
