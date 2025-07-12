package it.trenical.customer.gui.ui.viewModels

import it.trenical.customer.gui.data.grpc.TrenicalClient
import it.trenical.customer.gui.data.models.QueryParams
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
    private val _queryState = MutableStateFlow<QueryParams>(QueryParams(count = 1))
    val queryState: StateFlow<QueryParams> = _queryState.asStateFlow()
    private val _dsState = MutableStateFlow<QueryDataSource?>(null)
    val dsState: StateFlow<QueryDataSource?> = _dsState.asStateFlow()

    init {
        scope.launch {
            val trainTypes = async { trenicalClient.getAllTrainTypes() }
            val serviceClasses = async { trenicalClient.getAllServiceClasses() }
            val stations = async { trenicalClient.getAllStations() }
            awaitAll(trainTypes, serviceClasses, stations).let { (trainTypes, serviceClasses, stations) ->
                _dsState.value = QueryDataSource(
                    stations = stations,
                    serviceClasses = serviceClasses,
                    trainTypes = trainTypes,
                )
            }

        }
    }

    fun setDeparture(station: String?) {
        _queryState.getAndUpdate { it.copy(departure = station) }
    }

    fun setArrival(station: String?) {
        _queryState.getAndUpdate { it.copy(arrival = station) }
    }

    fun setCount(count: Int?) {
        _queryState.getAndUpdate { it.copy(count = count ?: 1) }
    }

    fun setTrainType(type: String?) {
        _queryState.getAndUpdate { it.copy(trainType = type) }
    }

    fun setServiceClass(serviceClass: String?) {
        _queryState.getAndUpdate { it.copy(serviceClass = serviceClass) }
    }

    fun setdate(date: Long?){
        _queryState.getAndUpdate { it.copy(date = date) }}

    data class QueryDataSource(
        val stations : List<String>,
        val trainTypes: List<String>,
        val serviceClasses: List<String>
    )
}