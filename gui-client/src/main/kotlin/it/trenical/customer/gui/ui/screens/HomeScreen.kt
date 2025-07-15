package it.trenical.customer.gui.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import it.trenical.customer.gui.data.LoyaltyManager
import it.trenical.customer.gui.data.NotificationManager
import it.trenical.customer.gui.data.grpc.TrenicalClient
import it.trenical.customer.gui.data.models.AuthState
import it.trenical.customer.gui.ui.components.FedeltaTrenoCard
import it.trenical.customer.gui.ui.components.NotificationsCard
import it.trenical.customer.gui.ui.components.PurchaseDialog
import it.trenical.customer.gui.ui.components.SearchPanel
import it.trenical.customer.gui.ui.components.SearchResults
import it.trenical.customer.gui.ui.viewModels.HomeViewModel
import it.trenical.customer.gui.ui.viewModels.PurchaseViewModel
import it.trenical.customer.gui.ui.viewModels.TravelSearchViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(authState: AuthState.Ready) {
    val trenicalClient = remember { TrenicalClient(authState.token) }
    val loyaltyManager = remember { LoyaltyManager(trenicalClient) }
    val purchaseViewModel = remember { PurchaseViewModel(trenicalClient) }
    val viewModel = remember { HomeViewModel(trenicalClient) }
    val searchViewModel = remember { TravelSearchViewModel(trenicalClient) }
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        TopAppBar(
            title = { Text("Trenical", fontWeight = FontWeight.Bold) },
            actions = {
                Button(content = { Text("Trova treno") }, onClick = {})
                Spacer(Modifier.width(16.dp))
                Button(content = { Text("I miei biglietti") }, onClick = {})
            }
        )
        Row(Modifier.fillMaxWidth()) {
            Column(Modifier.fillMaxWidth(0.7f), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(16.dp))
                SearchPanel(viewModel, searchViewModel::searchTravels)
                SearchResults(searchViewModel, purchaseViewModel)
            }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                FedeltaTrenoCard(loyaltyManager)
                NotificationsCard(NotificationManager)
            }
        }
    }
    PurchaseDialog(purchaseViewModel)
}