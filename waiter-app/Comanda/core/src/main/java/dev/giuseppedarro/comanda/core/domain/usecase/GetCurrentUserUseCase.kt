package dev.giuseppedarro.comanda.core.domain.usecase

import dev.giuseppedarro.comanda.core.domain.model.UserProfile
import dev.giuseppedarro.comanda.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentUserUseCase(private val repository: UserRepository) {
    operator fun invoke(): Flow<UserProfile?> = repository.getCurrentUser()
}
