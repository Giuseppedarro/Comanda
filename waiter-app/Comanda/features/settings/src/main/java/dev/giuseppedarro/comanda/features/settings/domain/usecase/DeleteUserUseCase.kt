package dev.giuseppedarro.comanda.features.settings.domain.usecase

import dev.giuseppedarro.comanda.features.settings.domain.repository.UserRepository

class DeleteUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(id: String): Result<Unit> = repository.deleteUser(id)
}
