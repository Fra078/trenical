package it.trenical.customer.gui.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import it.trenical.customer.gui.data.LoyaltyManager

@Composable
fun FedeltaTrenoCard(
    loyaltyManager: LoyaltyManager,
    modifier: Modifier = Modifier
) {
    val state by loyaltyManager.state.collectAsState()
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Programma FedeltaTreno",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(visible = state.subscriptionDate != null) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Abilita notifiche",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = state.listeningEnabled,
                            onCheckedChange = {
                                if (it) loyaltyManager.listenToPromotions()
                                else loyaltyManager.stopListening()
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = loyaltyManager::unsubscribeLoyalty,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Text("Abbandona il programma")
                    }
                }
            }

            AnimatedVisibility(visible = state.subscriptionDate == null) {
                Column {
                    Text(
                        text = "Aderisci per accedere a sconti e offerte esclusive.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = loyaltyManager::subscribeLoyalty) {
                        Text("Aderisci Ora")
                    }
                }
            }
        }
    }
}