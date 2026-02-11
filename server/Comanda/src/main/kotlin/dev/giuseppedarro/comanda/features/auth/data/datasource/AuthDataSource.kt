package dev.giuseppedarro.comanda.features.auth.data.datasource

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.giuseppedarro.comanda.features.auth.data.model.LoginRequest
import dev.giuseppedarro.comanda.features.auth.data.model.LoginResponse
import dev.giuseppedarro.comanda.features.auth.data.model.RefreshTokenRequest
import dev.giuseppedarro.comanda.features.auth.data.model.TokenResponse
import dev.giuseppedarro.comanda.features.users.data.Users
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import java.util.*

class AuthDataSource(
    private val jwtAudience: String,
    private val jwtDomain: String,
    private val jwtSecret: String
) {

    suspend fun login(request: LoginRequest): Result<LoginResponse> {
        // 1. Fetch user by employeeId
        val userRow = transaction {
            Users.select { Users.employeeId eq request.employeeId }.singleOrNull()
        }

        if (userRow != null) {
            val storedPassword = userRow[Users.password]
            
            // 2. Verify Password (Hybrid: Hash or Plain Text)
            val isPasswordValid = try {
                // Try to verify as BCrypt hash
                BCrypt.checkpw(request.password, storedPassword)
            } catch (e: Exception) {
                // If checkpw fails (e.g. storedPassword is not a valid hash), fallback to plain text check
                storedPassword == request.password
            }

            if (isPasswordValid) {
                val employeeId = userRow[Users.employeeId]
                val role = userRow[Users.role]
                val name = userRow[Users.name]
                
                val accessToken = generateAccessToken(employeeId, role, name)
                val refreshToken = generateRefreshToken(employeeId)
                return Result.success(LoginResponse(accessToken, refreshToken))
            }
        }
        
        return Result.failure(Exception("Invalid credentials"))
    }

    suspend fun refreshToken(request: RefreshTokenRequest): Result<TokenResponse> {
        val decodedJWT = JWT.decode(request.refreshToken)
        val employeeId = decodedJWT.getClaim("userId").asString()

        val userRow = transaction {
            Users.select { Users.employeeId eq employeeId }.singleOrNull()
        }

        if (userRow != null) {
            val role = userRow[Users.role]
            val name = userRow[Users.name]
            
            val newAccessToken = generateAccessToken(employeeId, role, name)
            val newRefreshToken = generateRefreshToken(employeeId)
            return Result.success(TokenResponse(newAccessToken, newRefreshToken))
        } else {
            return Result.failure(Exception("User not found"))
        }
    }

    private fun generateAccessToken(employeeId: String, role: String, name: String): String {
        return JWT.create()
            .withAudience(jwtAudience)
            .withIssuer(jwtDomain)
            .withClaim("userId", employeeId)
            .withClaim("name", name)
            .withClaim("role", role)
            .withExpiresAt(Date(System.currentTimeMillis() + 600000)) // 10 minutes
            .sign(Algorithm.HMAC256(jwtSecret))
    }

    private fun generateRefreshToken(employeeId: String): String {
        return JWT.create()
            .withAudience(jwtAudience)
            .withIssuer(jwtDomain)
            .withClaim("userId", employeeId)
            .withExpiresAt(Date(System.currentTimeMillis() + 604800000)) // 7 days
            .sign(Algorithm.HMAC256(jwtSecret))
    }
}
