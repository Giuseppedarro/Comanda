package dev.giuseppedarro.comanda.core.domain.usecase

import dev.giuseppedarro.comanda.core.domain.model.UserProfile
import dev.giuseppedarro.comanda.core.domain.repository.TokenRepository
import dev.giuseppedarro.comanda.core.domain.repository.UserRepository
import dev.giuseppedarro.comanda.core.utils.JwtDecoder

/**
 * Syncs the current user's profile by retrieving the access token,
 * decoding the user ID, and fetching the profile from the repository.
 */
class SyncUserProfileUseCase(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val jwtDecoder: JwtDecoder
) {
    suspend operator fun invoke(): Result<UserProfile> {
        return try {
            val token = tokenRepository.getAccessToken() 
                ?: return Result.failure(Exception("No access token found"))
            
            val userId = jwtDecoder.getUserId(token)
                ?: return Result.failure(Exception("Could not extract user ID from token"))
            
            userRepository.fetchUserProfile(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
