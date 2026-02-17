package dev.giuseppedarro.comanda.features.settings.domain.use_case

import dev.giuseppedarro.comanda.features.settings.data.remote.dto.UpdateUserRequest
import dev.giuseppedarro.comanda.features.settings.domain.model.User
import dev.giuseppedarro.comanda.features.settings.domain.repository.UserRepository

class UpdateUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(id: String, request: UpdateUserRequest): Result<User> = repository.updateUser(id, request)
}
