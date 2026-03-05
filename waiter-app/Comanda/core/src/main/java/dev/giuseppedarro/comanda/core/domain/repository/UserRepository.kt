package dev.giuseppedarro.comanda.core.domain.repository

import dev.giuseppedarro.comanda.core.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getCurrentUser(): Flow<UserProfile?>
    suspend fun fetchUserProfile(id: Int): Result<UserProfile>
}
