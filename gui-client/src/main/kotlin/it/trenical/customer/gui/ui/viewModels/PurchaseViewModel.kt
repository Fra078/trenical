package it.trenical.customer.gui.ui.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import it.trenical.customer.gui.data.grpc.TrenicalClient
import it.trenical.ticketry.proto.purchaseTicketRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PurchaseViewModel(private val client: TrenicalClient) {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val _state = MutableStateFlow<State?>(null)
    val state = _state.asStateFlow()

    fun requestPurchase(
        serviceClass: String,
        trainId: Int,
        departure: String,
        arrival: String,
        ticketCount: Int) {
        _state.value = State(serviceClass, trainId, departure, arrival, ticketCount)
    }

    fun cancelPurchase() {
        _state.value = null
    }

    fun confirm(creditCard: String) {
        scope.launch {
            val current = _state.value!!
            client.makePurchase(purchaseTicketRequest {
                this.creditCard = creditCard
                this.serviceClass = current.serviceClass
                this.trainId = current.trainId
                this.departure = current.departure
                this.arrival = current.arrival
                this.ticketCount = current.ticketCount
            })
        }
    }

    data class State(
        val serviceClass: String,
        val trainId: Int,
        val departure: String,
        val arrival: String,
        val ticketCount: Int
    )
}