package it.trenical.customer.gui.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import it.trenical.customer.gui.data.AuthState
import it.trenical.customer.gui.data.grpc.TrenicalClient
import it.trenical.customer.gui.ui.components.DatePickerFieldToModal
import it.trenical.customer.gui.ui.viewModels.HomeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(authState: AuthState.Ready) {
    val trenicalClient = remember { TrenicalClient(authState.token) }
    val viewModel = remember { HomeViewModel(trenicalClient) }
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

        SearchPanel(viewModel)

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchPanel(viewModel: HomeViewModel){
    val state = viewModel.state.collectAsState().value
    if (state != null)
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(0.75f)
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Ricerca soluzioni di viaggio",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(16.dp))

        Column {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                MinimalDropdownMenu(
                    label = "Partenza",
                    value = state.departure,
                    options = state.stations,
                    action = viewModel::setDeparture
                )
                MinimalDropdownMenu(
                    label = "Destinazione",
                    value = state.arrival,
                    options = state.stations,
                    action = viewModel::setArrival
                )
                DatePickerFieldToModal(value = state.date, setValue = viewModel::setdate)
            }
            Spacer(Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                MinimalDropdownMenu(
                    label = "Tipologia",
                    value = state.trainType,
                    options = state.trainTypes,
                    action = viewModel::setTrainType
                )
                MinimalDropdownMenu(
                    label = "Classe",
                    value = state.serviceClass,
                    options = state.serviceClasses,
                    action = viewModel::setServiceClass
                )
                MinimalDropdownMenu(
                    label = "N° viaggiatori",
                    value = state.count,
                    options = IntRange(1,5),
                    action = viewModel::setCount
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {},
            enabled = state.isValid,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            content = { Text("Avvia ricerca") }
        )

        Spacer(Modifier.height(16.dp))
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> MinimalDropdownMenu(
    label: String,
    value: T?,
    options: Iterable<T>,
    action: (T?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = it },
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable),
            readOnly = true,
            value = value?.toString() ?: "",
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
                    text = { Text(selectionOption.toString()) },
                    onClick = {
                        action(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}