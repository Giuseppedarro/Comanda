package dev.giuseppedarro.comanda.features.settings.domain.use_case

import dev.giuseppedarro.comanda.features.settings.data.remote.dto.CreateUserRequest
import dev.giuseppedarro.comanda.features.settings.domain.model.User
import dev.giuseppedarro.comanda.features.settings.domain.repository.UserRepository

class CreateUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(createUserRequest: CreateUserRequest): Result<User> {
        return userRepository.createUser(createUserRequest)
    }
}
