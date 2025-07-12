package it.trenical.customer.gui.ui.viewModels

import androidx.compose.runtime.mutableStateSetOf
import it.trenical.customer.gui.data.grpc.TrenicalClient
import it.trenical.customer.gui.data.models.QueryParams
import it.trenical.travel.proto.TravelSolution
import it.trenical.travel.proto.copy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.TreeSet

class TravelSearchViewModel(private val trenicalClient: TrenicalClient) {
    private val scope = CoroutineScope(Dispatchers.IO)
    private var searchJob : Job? = null
    private val _state = MutableStateFlow<List<TravelSolution>>(emptyList())
    val state = _state.asStateFlow()

    fun searchTravels(params: QueryParams){
        searchJob?.cancel()
        _state.value = listOf<TravelSolution>()
        val backingSet = TreeSet<TravelSolution>(comparator)
        searchJob = trenicalClient.queryTravel(params)
            .onEach {
                backingSet.add(it)
                _state.value = backingSet.toList()
            }
            .launchIn(scope)
    }

    companion object {
        private val comparator = compareBy<TravelSolution> { t->
            t.routeInfo.departureTime
        }
    }

}