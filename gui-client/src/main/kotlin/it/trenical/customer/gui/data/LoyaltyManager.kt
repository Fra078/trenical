package it.trenical.customer.gui.data

import it.trenical.customer.gui.data.grpc.TrenicalClient
import it.trenical.customer.gui.data.models.Notification
import it.trenical.promotion.proto.GetSubscriptionInfoResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.Instant

class LoyaltyManager(private val client: TrenicalClient) {
    private val scope = CoroutineScope(Dispatchers.IO)
    private var listeningJob : Job? = null
    private val _state : MutableStateFlow<State> = MutableStateFlow(State(null, false))
    val state = _state.asStateFlow()

    init {
        scope.launch {
            val response = client.getLoyaltySubscription()
            if (response.subscribed)
                _state.value = State(response.fromDate, false)
        }
    }

    fun subscribeLoyalty() {
        scope.launch {
            val done = runCatching { client.subscribeLoyalty() }.isSuccess
            if (done)
                _state.value = State(Instant.now().epochSecond, false)
        }
    }

    fun unsubscribeLoyalty() {
        scope.launch {
            val done = runCatching { client.unsubscribeLoyalty() }.isSuccess
            if (done)
                _state.value = State(null, false)
        }
    }

    fun listenToPromotions(){
        listeningJob?.cancel()
        listeningJob = client.listenToPromotions().onEach {
            NotificationManager.emit("Nuova promo esclusiva", it.name + ": " + it.description)
        }.launchIn(scope)
        _state.getAndUpdate { it.copy(listeningEnabled = true) }
    }

    fun stopListening(){
        listeningJob?.cancel()
        _state.getAndUpdate { it.copy(listeningEnabled = false) }
    }

    data class State(val subscriptionDate: Long?, val listeningEnabled: Boolean)

}