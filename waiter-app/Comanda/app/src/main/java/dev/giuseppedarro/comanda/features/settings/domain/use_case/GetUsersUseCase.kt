package dev.giuseppedarro.comanda.features.settings.domain.use_case

import dev.giuseppedarro.comanda.features.settings.domain.model.User
import dev.giuseppedarro.comanda.features.settings.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUsersUseCase(private val repository: UserRepository) {
    operator fun invoke(): Flow<Result<List<User>>> = repository.getUsers()
}
