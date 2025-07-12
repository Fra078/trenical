package it.trenical.customer.gui.ui.viewModels

import it.trenical.customer.gui.data.grpc.TrenicalClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch

class HomeViewModel(private val trenicalClient: TrenicalClient) {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val _state = MutableStateFlow<QueryState?>(null)
    val state: StateFlow<QueryState?> = _state.asStateFlow()

    init {
        scope.launch {
            val trainTypes = async { trenicalClient.getAllTrainTypes() }
            val serviceClasses = async { trenicalClient.getAllServiceClasses() }
            val stations = async { trenicalClient.getAllStations() }
            awaitAll(trainTypes, serviceClasses, stations).let { (trainTypes, serviceClasses, stations) ->
                _state.value = QueryState(
                    stations = stations,
                    serviceClasses = serviceClasses,
                    trainTypes = trainTypes,
                )
            }

        }

    }

    fun setDeparture(station: String?) {
        _state.getAndUpdate { it?.copy(departure = station) }
    }

    fun setArrival(station: String?) {
        _state.getAndUpdate { it?.copy(arrival = station) }
    }

    fun setCount(count: Int?) {
        _state.getAndUpdate { it?.copy(count = count ?: 1) }
    }

    fun setTrainType(type: String?) {
        _state.getAndUpdate { it?.copy(trainType = type) }
    }

    fun setServiceClass(serviceClass: String?) {
        _state.getAndUpdate { it?.copy(serviceClass = serviceClass) }
    }

    fun setdate(date: Long?){
        _state.getAndUpdate { it?.copy(date = date) }}

    data class QueryState(
        val stations : List<String>,
        val trainTypes: List<String>,
        val serviceClasses: List<String>,
        val departure: String? = null,
        val arrival: String? = null,
        val date: Long? = null,
        val trainType: String? = null,
        val serviceClass: String? = null,
        val count: Int = 1
    ){
        val isValid = departure != null && arrival != null && date != null
    }
}