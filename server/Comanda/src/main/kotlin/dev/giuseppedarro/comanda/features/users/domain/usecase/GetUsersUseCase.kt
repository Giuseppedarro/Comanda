package dev.giuseppedarro.comanda.features.users.domain.usecase

import dev.giuseppedarro.comanda.features.users.domain.model.User
import dev.giuseppedarro.comanda.features.users.domain.repository.UsersRepository

class GetUsersUseCase(private val usersRepository: UsersRepository) {
    suspend operator fun invoke(): Result<List<User>> {
        return usersRepository.getUsers()
    }
}
