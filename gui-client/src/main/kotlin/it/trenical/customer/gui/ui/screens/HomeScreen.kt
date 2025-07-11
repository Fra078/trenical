package it.trenical.customer.gui.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import it.trenical.customer.gui.data.AuthState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(authState: AuthState.Ready) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        TopAppBar(
            title = { Text("Trenical") },
            actions = {
                Button(content = {Text("Trova treno")}, onClick = {})
                Spacer(Modifier.width(16.dp))
                Button(content = {Text("I miei biglietti")}, onClick = {})
            }
        )
        Spacer(Modifier.height(16.dp))
        Text("Benvenuto ${authState.firstName}!")

        SearchPanel()

    }
}

@Composable
private fun SearchPanel(){
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(0.5f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Text(
            text = "Ricerca soluzioni di viaggio",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(16.dp))

        Column {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                MinimalDropdownMenu("Partenza")
                MinimalDropdownMenu("Destinazione")
            }
            Spacer(Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                MinimalDropdownMenu("Tipo treno")
                MinimalDropdownMenu("Classe")
                MinimalDropdownMenu("Numero posti")
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {},
            modifier = Modifier.align(Alignment.CenterHorizontally),
            content = { Text("Avvia ricerca") }
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinimalDropdownMenu(label: String) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") }
    val options = listOf("A", "B", "C", "D", "E")
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            println("onExpandedChange: $it")
            expanded = it },
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    }
                )
            }
        }
    }
}
