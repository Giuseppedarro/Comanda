package dev.giuseppedarro.comanda.features.auth.data.datasource

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.giuseppedarro.comanda.features.auth.data.model.LoginRequest
import dev.giuseppedarro.comanda.features.auth.data.model.LoginResponse
import dev.giuseppedarro.comanda.features.auth.data.model.RefreshTokenRequest
import dev.giuseppedarro.comanda.features.auth.data.model.TokenResponse
import java.util.*

class AuthDataSource {
    // Please read the jwt property from the config file.
    private val jwtAudience = "jwt-audience"
    private val jwtDomain = "https://jwt-provider-domain/"
    private val jwtSecret = "secret"

    suspend fun login(request: LoginRequest): Result<LoginResponse> {
        if (request.employeeId == "1234" && request.password == "password") {
            val accessToken = generateAccessToken(request.employeeId, "WAITER")
            val refreshToken = generateRefreshToken(request.employeeId)
            return Result.success(LoginResponse(accessToken, refreshToken))
        } else {
            return Result.failure(Exception("Invalid credentials"))
        }
    }

    suspend fun refreshToken(request: RefreshTokenRequest): Result<TokenResponse> {
        // For now, we'll just generate a new pair of tokens without validating the refresh token.
        // In a real implementation, we would validate the refresh token first.
        val decodedJWT = JWT.decode(request.refreshToken)
        val employeeId = decodedJWT.getClaim("userId").asString()

        // In a real implementation, we would look up the user's role from the database
        // For this stub, we'll just hardcode it.
        val role = "WAITER"

        val newAccessToken = generateAccessToken(employeeId, role)
        val newRefreshToken = generateRefreshToken(employeeId)

        return Result.success(TokenResponse(newAccessToken, newRefreshToken))
    }

    private fun generateAccessToken(employeeId: String, role: String): String {
        return JWT.create()
            .withAudience(jwtAudience)
            .withIssuer(jwtDomain)
            .withClaim("userId", employeeId)
            .withClaim("name", "Waiter Name") // Placeholder name
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
