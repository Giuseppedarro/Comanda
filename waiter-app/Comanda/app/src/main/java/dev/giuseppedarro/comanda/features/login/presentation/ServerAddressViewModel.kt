package dev.giuseppedarro.comanda.features.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.giuseppedarro.comanda.features.login.domain.use_case.GetServerAddressUseCase
import dev.giuseppedarro.comanda.features.login.domain.use_case.SetServerAddressUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ServerAddressViewModel(
    private val getServerAddressUseCase: GetServerAddressUseCase,
    private val setServerAddressUseCase: SetServerAddressUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow("")
    val uiState = _uiState.asStateFlow()

    init {
        getServerAddressUseCase()
            .onEach { address -> _uiState.update { address } }
            .launchIn(viewModelScope)
    }

    fun onAddressChange(newAddress: String) {
        _uiState.update { newAddress }
    }

    fun saveAddress() {
        viewModelScope.launch {
            setServerAddressUseCase(uiState.value)
        }
    }
}
