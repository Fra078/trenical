package it.trenical.customer.gui.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import it.trenical.customer.gui.data.models.AuthState
import it.trenical.customer.gui.data.grpc.TrenicalClient
import it.trenical.customer.gui.data.models.QueryParams
import it.trenical.customer.gui.ui.components.DatePickerFieldToModal
import it.trenical.customer.gui.ui.components.DropdownSelector
import it.trenical.customer.gui.ui.viewModels.HomeViewModel
import it.trenical.customer.gui.ui.viewModels.TravelSearchViewModel
import it.trenical.travel.proto.TravelSolution
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(authState: AuthState.Ready) {
    val trenicalClient = remember { TrenicalClient(authState.token) }
    val viewModel = remember { HomeViewModel(trenicalClient) }
    val searchViewModel = remember { TravelSearchViewModel(trenicalClient) }
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

        SearchPanel(viewModel, searchViewModel::searchTravels)
        SearchResults(searchViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchPanel(viewModel: HomeViewModel, searchAction: (QueryParams)->Unit) {
    val dataSource = viewModel.dsState.collectAsState().value
    val state = viewModel.queryState.collectAsState().value
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

@Composable
private fun SearchResults(viewModel: TravelSearchViewModel){
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxWidth(0.5f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            items = state,
            key = { it.trainId }
        ){
            TravelSolutionCard(it)
        }
    }

}

@Composable
fun TravelSolutionCard(solution: TravelSolution, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("${solution.trainName} (${solution.trainId}) - ${solution.type.name}")
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = solution.routeInfo.departureTime.asTime(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = solution.routeInfo.departureStation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1.5f)
                ) {
                    Text(
                        text = solution.routeInfo.distance.approx() + " km",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Distanza",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = solution.routeInfo.arrivalTime.asTime(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = solution.routeInfo.arrivalStation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.End
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                solution.modesList.forEach { mode ->
                    ServicePriceRow(mode)
                }
            }
        }
    }
}

@Composable
private fun ServicePriceRow(mode: TravelSolution.Mode) {
    val basePrice = mode.price
    val promoPrice = if (mode.hasPromo()) mode.promo.finalPrice else null
    val finalPrice = (promoPrice ?: basePrice).approx()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = mode.serviceClass.name,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (promoPrice != null){
                Text(
                    text = "€ ${basePrice.approx()}",
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = TextDecoration.LineThrough,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
            }
            Button(
                onClick = {},
                modifier = Modifier.width(120.dp),
                content = {
                    Text(
                        text = "€ $finalPrice",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                    )
                }
            )
        }
    }
}

private fun Double.approx() = String.format("%.2f", this)
private fun Long.asTime() =
    DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault())
        .format(Instant.ofEpochSecond(this))
