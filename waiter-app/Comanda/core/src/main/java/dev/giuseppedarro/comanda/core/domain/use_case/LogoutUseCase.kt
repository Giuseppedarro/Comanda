package dev.giuseppedarro.comanda.core.domain.use_case

import dev.giuseppedarro.comanda.core.domain.TokenRepository

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
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}