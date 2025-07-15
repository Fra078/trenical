package it.trenical.customer.gui.ui.components

import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import it.trenical.customer.gui.ui.viewModels.PurchaseViewModel

@Composable
fun PurchaseDialog(viewModel: PurchaseViewModel) {
    val state = viewModel.state.collectAsState().value
    if (state == null)
        return

    var creditCard by remember { mutableStateOf("") }

    CommonDialog(
        onDismissRequest = {viewModel.cancelPurchase()},
        onConfirmation = {viewModel.confirm(creditCard)},
        title = "Purchase ticket",
        canConfirm = creditCard.length >=4,
        content = {
            OutlinedTextField(
                value = creditCard,
                onValueChange = { creditCard = it },
            )
        }
    )
}