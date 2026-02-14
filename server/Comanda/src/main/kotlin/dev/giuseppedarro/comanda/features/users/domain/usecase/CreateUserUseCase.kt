package dev.giuseppedarro.comanda.features.users.domain.usecase

import dev.giuseppedarro.comanda.features.users.domain.model.User
import dev.giuseppedarro.comanda.features.users.domain.repository.UsersRepository
import org.mindrot.jbcrypt.BCrypt

data class CreateUserParams(
    val employeeId: String,
    val name: String,
    val password: String,
    val role: String
)

class CreateUserUseCase(private val usersRepository: UsersRepository) {
    suspend operator fun invoke(params: CreateUserParams): Result<User> {
        // Validate input
        if (params.password.length < 4) {
            return Result.failure(IllegalArgumentException("Password must be at least 4 characters long"))
        }

        // Hash password
        val passwordHash = BCrypt.hashpw(params.password, BCrypt.gensalt())

        // Create user
        return usersRepository.createUser(
            employeeId = params.employeeId,
            name = params.name,
            passwordHash = passwordHash,
            role = params.role
        )
    }
}
