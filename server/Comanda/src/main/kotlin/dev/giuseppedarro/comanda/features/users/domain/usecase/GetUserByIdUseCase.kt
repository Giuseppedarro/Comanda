package dev.giuseppedarro.comanda.features.users.domain.usecase

import dev.giuseppedarro.comanda.features.users.domain.model.User
import dev.giuseppedarro.comanda.features.users.domain.repository.UsersRepository

class GetUserByIdUseCase(private val usersRepository: UsersRepository) {
    suspend operator fun invoke(id: Int): Result<User> {
        return usersRepository.getUserById(id)
    }
}
