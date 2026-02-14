package dev.giuseppedarro.comanda.features.users.domain.usecase

import dev.giuseppedarro.comanda.features.users.domain.model.User
import dev.giuseppedarro.comanda.features.users.domain.repository.UsersRepository
import org.mindrot.jbcrypt.BCrypt

data class UpdateUserParams(
    val id: Int,
    val employeeId: String? = null,
    val name: String? = null,
    val password: String? = null,
    val role: String? = null
)

class UpdateUserUseCase(private val usersRepository: UsersRepository) {
    suspend operator fun invoke(params: UpdateUserParams): Result<User> {
        // Validate password if provided
        if (params.password != null && params.password.length < 4) {
            return Result.failure(IllegalArgumentException("Password must be at least 4 characters long"))
        }

        // Hash password if provided
        val passwordHash = if (params.password != null) {
            BCrypt.hashpw(params.password, BCrypt.gensalt())
        } else {
            null
        }

        // Update user
        return usersRepository.updateUser(
            id = params.id,
            employeeId = params.employeeId,
            name = params.name,
            passwordHash = passwordHash,
            role = params.role
        )
    }
}
