package dev.giuseppedarro.comanda.core.data.repository

import dev.giuseppedarro.comanda.core.domain.model.DomainException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import java.io.IOException

/**
 * Maps common technical exceptions (Network, Ktor, etc.) to [DomainException].
 */
fun Throwable.toDomainException(): DomainException {
    return when (this) {
        is IOException -> DomainException.NetworkException
        is ClientRequestException -> {
            when (response.status) {
                HttpStatusCode.Unauthorized -> DomainException.UnauthorizedException
                HttpStatusCode.NotFound -> DomainException.NotFoundException
                else -> DomainException.UnknownException(message)
            }
        }
        is ServerResponseException -> DomainException.ServerException
        is RedirectResponseException -> DomainException.UnknownException("Redirect error: ${message}")
        is DomainException -> this
        else -> DomainException.UnknownException(message)
    }
}

/**
 * Executes a network call and wraps it in a [Result], mapping any thrown exceptions
 * to [DomainException] via [toDomainException].
 */
suspend fun <T> safeApiCall(call: suspend () -> T): Result<T> {
    return try {
        Result.success(call())
    } catch (e: Exception) {
        Result.failure(e.toDomainException())
    }
}

/**
 * Maps HttpStatusCode to [DomainException].
 */
fun HttpStatusCode.toDomainException(): DomainException {
    return when (this) {
        HttpStatusCode.Unauthorized -> DomainException.UnauthorizedException
        HttpStatusCode.NotFound -> DomainException.NotFoundException
        HttpStatusCode.InternalServerError -> DomainException.ServerException
        else -> DomainException.UnknownException("HTTP error: $value")
    }
}
