package it.trenical.customer.gui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import it.trenical.customer.gui.data.AuthState
import it.trenical.customer.gui.data.AuthManager
import it.trenical.customer.gui.ui.screens.HomeScreen
import it.trenical.customer.gui.ui.screens.LoginScreen


fun main() = singleWindowApplication(
    title = "Trenical",
    state = WindowState(size = DpSize(800.dp, 600.dp)),
) {
    MaterialTheme {
        MainScreen()
    }
}

@Composable
private fun MainScreen() {
    val authManager = remember { AuthManager() }
    val authState = authManager.state.collectAsState().value

    when (authState) {
        AuthState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        AuthState.Login -> LoginScreen(authManager)
        is AuthState.Ready -> HomeScreen(authState)
    }
}
