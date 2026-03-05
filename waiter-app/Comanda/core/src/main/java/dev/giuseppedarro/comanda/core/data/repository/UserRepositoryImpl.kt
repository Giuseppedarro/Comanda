package dev.giuseppedarro.comanda.core.data.repository

import dev.giuseppedarro.comanda.core.domain.model.UserProfile
import dev.giuseppedarro.comanda.core.domain.repository.UserRepository
import dev.giuseppedarro.comanda.core.network.UserApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserRepositoryImpl(
    private val userApi: UserApi
) : UserRepository {

    private val _currentUser = MutableStateFlow<UserProfile?>(null)

    override fun getCurrentUser(): Flow<UserProfile?> = _currentUser.asStateFlow()

    override suspend fun fetchUserProfile(id: Int): Result<UserProfile> {
        return try {
            val profile = userApi.getUserProfile(id)
            _currentUser.value = profile
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
