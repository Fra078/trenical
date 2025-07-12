package it.trenical.customer.gui.data

import it.trenical.customer.gui.data.grpc.GrpcChannelProvider
import it.trenical.customer.gui.data.grpc.LoginClient
import it.trenical.customer.gui.data.models.AuthState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthManager {
    private val loginClient = LoginClient()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val _state = MutableStateFlow<AuthState>(AuthState.Login)
    val state = _state.asStateFlow()

    fun login(username: String, password: String) {
        coroutineScope.launch {
            _state.value = AuthState.Loading
            val result = runCatching { loginClient.login(username, password) }
            if (result.isSuccess) {
                val response = result.getOrNull()!!
                _state.value = AuthState.Ready(response.jwt, response.username, response.firstName, response.lastName)
            } else {
                _state.value = AuthState.Login
            }
        }
    }

    fun register(username: String, password: String, firstName: String, lastName: String) {
        coroutineScope.launch {
            _state.value = AuthState.Loading
            val result = runCatching { loginClient.register(username, password, firstName, lastName) }
            if (result.isSuccess) {
                val response = result.getOrNull()!!
                _state.value = AuthState.Ready(response.jwt, response.username, response.firstName, response.lastName)
            } else {
                _state.value = AuthState.Login
            }
        }
    }


}