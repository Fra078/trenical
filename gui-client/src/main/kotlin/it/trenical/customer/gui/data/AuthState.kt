package it.trenical.customer.gui.data

sealed interface AuthState {
    data object Loading : AuthState
    data object Login : AuthState
    data class Ready(val token: String,
                     val username: String,
                     val firstName: String,
                     val lastName: String) : AuthState
}