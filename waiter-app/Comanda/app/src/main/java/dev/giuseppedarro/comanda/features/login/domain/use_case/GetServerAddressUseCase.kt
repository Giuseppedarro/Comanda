package dev.giuseppedarro.comanda.features.login.domain.use_case

import dev.giuseppedarro.comanda.core.domain.ServerAddressRepository
import kotlinx.coroutines.flow.Flow

class GetServerAddressUseCase(private val repository: ServerAddressRepository) {
    operator fun invoke(): Flow<String> = repository.getAddress()
}
