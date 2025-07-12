package it.trenical.customer.gui.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import it.trenical.customer.gui.data.models.QueryParams
import it.trenical.customer.gui.ui.viewModels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPanel(viewModel: HomeViewModel, searchAction: (QueryParams)->Unit) {
    val dataSource = viewModel.dsState.collectAsState().value
    val state = viewModel.queryState.collectAsState().value
    Card(
        modifier = Modifier.padding(16.dp),
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
                DropdownSelector(
                    label = "Partenza",
                    value = state.departure,
                    options = dataSource?.stations,
                    action = viewModel::setDeparture
                )
                DropdownSelector(
                    label = "Destinazione",
                    value = state.arrival,
                    options = dataSource?.stations,
                    action = viewModel::setArrival
                )
                DatePickerFieldToModal(value = state.date, setValue = viewModel::setdate)
            }
            Spacer(Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                DropdownSelector(
                    label = "Tipologia",
                    value = state.trainType,
                    options = dataSource?.trainTypes,
                    action = viewModel::setTrainType
                )
                DropdownSelector(
                    label = "Classe",
                    value = state.serviceClass,
                    options = dataSource?.serviceClasses,
                    action = viewModel::setServiceClass
                )
                DropdownSelector(
                    label = "N° viaggiatori",
                    value = state.count,
                    options = IntRange(1,5),
                    action = viewModel::setCount
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { searchAction(state) },
            enabled = state.isValid,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            content = { Text("Avvia ricerca") }
        )

        Spacer(Modifier.height(16.dp))
    }
}
