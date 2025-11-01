package dev.giuseppedarro.comanda.features.login.domain.use_case

import dev.giuseppedarro.comanda.core.domain.ServerAddressRepository

class SetServerAddressUseCase(private val repository: ServerAddressRepository) {
    suspend operator fun invoke(address: String) {
        repository.setAddress(address)
    }
}
