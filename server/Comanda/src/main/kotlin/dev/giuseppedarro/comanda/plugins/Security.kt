package dev.giuseppedarro.comanda.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    // These values MUST match the ones in AuthDataSource.kt
    val jwtAudience = "jwt-audience"
    val jwtIssuer = "https://jwt-provider-domain/"
    val jwtRealm = "Comanda Server" // This can be any string
    val jwtSecret = "secret"

    install(Authentication) {
        jwt("auth-jwt") {
            realm = jwtRealm
            verifier(JWT
                .require(Algorithm.HMAC256(jwtSecret))
                .withAudience(jwtAudience)
                .withIssuer(jwtIssuer)
                .build())
            validate { credential ->
                if (credential.payload.getClaim("userId").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}
