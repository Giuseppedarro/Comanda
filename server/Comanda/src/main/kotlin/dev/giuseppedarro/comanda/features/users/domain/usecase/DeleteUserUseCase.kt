package dev.giuseppedarro.comanda.features.users.domain.usecase

import dev.giuseppedarro.comanda.features.users.domain.repository.UsersRepository

class DeleteUserUseCase(private val usersRepository: UsersRepository) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return usersRepository.deleteUser(id)
    }
}
