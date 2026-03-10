package dev.giuseppedarro.comanda.core.domain.model

abstract class DomainException(
    override val message: String? = null,
    override val cause: Throwable? = null
) : Exception(message, cause) {
    
    object NetworkException : DomainException("No internet connection or network error.")
    
    object UnauthorizedException : DomainException("User is not authorized.")
    
    object ServerException : DomainException("A server-side error occurred.")
    
    object NotFoundException : DomainException("The requested resource was not found.")
    
    data class UnknownException(val originalMessage: String? = null) : DomainException(originalMessage)
}
