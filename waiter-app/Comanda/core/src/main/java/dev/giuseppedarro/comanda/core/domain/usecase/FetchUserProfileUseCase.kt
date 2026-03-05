package dev.giuseppedarro.comanda.core.domain.usecase

import dev.giuseppedarro.comanda.core.domain.model.UserProfile
import dev.giuseppedarro.comanda.core.domain.repository.UserRepository

class FetchUserProfileUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(id: Int): Result<UserProfile> = repository.fetchUserProfile(id)
}
