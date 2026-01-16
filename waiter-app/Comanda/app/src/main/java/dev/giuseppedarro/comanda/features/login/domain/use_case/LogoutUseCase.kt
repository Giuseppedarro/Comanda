package dev.giuseppedarro.comanda.features.login.domain.use_case

import dev.giuseppedarro.comanda.core.domain.TokenRepository
import dev.giuseppedarro.comanda.core.utils.Result

/**
 * Use case for logging out a user.
 * Clears the stored authentication tokens.
 */
class LogoutUseCase(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            tokenRepository.clear()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to logout")
        }
    }
}