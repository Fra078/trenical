package it.trenical.customer.gui.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.trenical.customer.gui.data.AuthManager
import it.trenical.gui_client.generated.resources.Res
import it.trenical.gui_client.generated.resources.trenical_logo
import org.jetbrains.compose.resources.painterResource
import kotlin.reflect.KMutableProperty0

@Composable
fun LoginScreen(authManager: AuthManager) {
    Box(Modifier.background(MaterialTheme.colorScheme.secondaryContainer).fillMaxSize(), contentAlignment = Alignment.Center) {
        var registration by remember { mutableStateOf(false) }
        Card(modifier = Modifier, elevation = CardDefaults.cardElevation(16.dp)) {
            Column(
                Modifier.padding(8.dp).widthIn(max = 400.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painterResource(Res.drawable.trenical_logo),
                    contentDescription = "",
                    Modifier.widthIn(max=400.dp)
                )
                if (registration)
                    SignUpForm(authManager){ registration = !registration }
                else
                    SignInForm(authManager){ registration = !registration }
            }
        }
    }
}

@Composable
private fun Textbox(value: KMutableProperty0<String>, placeholder: String) {
    OutlinedTextField(
        value = value.get(),
        placeholder = { Text(placeholder) },
        shape = RoundedCornerShape(24.dp),
        onValueChange = value::set
    )
}

@Composable
private fun ColumnScope.SignUpForm(
    authManager: AuthManager,
    changeMode: () -> Unit
) {
    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    Textbox(firstName::value, "Nome")
    Textbox(lastName::value, "Cognome")
    Textbox(username::value, "Username")
    Textbox(password::value, "Password")

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
        OutlinedButton(
            modifier = Modifier.width(140.dp),
            onClick = changeMode,
            content = { Text("Accedi") }
        )
        Button(
            onClick = {
                authManager.register(username.value, password.value, firstName.value, lastName.value)
            },
            modifier = Modifier.width(140.dp),
            content = { Text("Registrati") }
        )
    }
}


@Composable
private fun ColumnScope.SignInForm(
    authManager: AuthManager,
    changeMode: () -> Unit
) {

    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    Textbox(username::value, "Username")
    Textbox(password::value, "Password")

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
        OutlinedButton(
            modifier = Modifier.width(140.dp),
            onClick = changeMode,
            content = { Text("Registrati") }
        )
        Button(
            onClick = {
                authManager.login(username.value, password.value)
            },
            modifier = Modifier.width(140.dp),
            content = { Text("Accedi") }
        )
    }
}