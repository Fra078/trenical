package it.trenical.customer.gui.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.trenical.gui_client.generated.resources.Res
import it.trenical.gui_client.generated.resources.trenical_logo
import org.jetbrains.compose.resources.painterResource
import kotlin.reflect.KMutableProperty0

@Composable
fun LoginScreen() {
    Box(Modifier.background(Color.Blue).fillMaxSize(), contentAlignment = Alignment.Center) {
        var isSigninUp by remember { mutableStateOf(false) }
        Card(Modifier, elevation = 10.dp) {
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
                if (isSigninUp)
                    SignUpForm(){ isSigninUp = !isSigninUp }
                else
                    SignInForm(){ isSigninUp = !isSigninUp }
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
            shape = ButtonDefaults.filledTonalShape,
            modifier = Modifier.width(140.dp),
            onClick = changeMode,
            content = { Text("Accedi") }
        )
        FilledTonalButton(
            onClick = {},
            modifier = Modifier.width(140.dp),
            content = { Text("Registrati") }
        )
    }
}


@Composable
private fun ColumnScope.SignInForm(
    changeMode: () -> Unit
) {

    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    Textbox(username::value, "Username")
    Textbox(password::value, "Password")

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
        OutlinedButton(
            shape = ButtonDefaults.filledTonalShape,
            modifier = Modifier.width(140.dp),
            onClick = changeMode,
            content = { Text("Registrati") }
        )
        FilledTonalButton(
            onClick = {},
            modifier = Modifier.width(140.dp),
            content = { Text("Accedi") }
        )
    }
}