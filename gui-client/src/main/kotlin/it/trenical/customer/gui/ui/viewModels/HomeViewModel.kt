package it.trenical.customer.gui.ui.viewModels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel() {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val _state = MutableStateFlow(QueryInfoState())
    val state: StateFlow<QueryInfoState> = _state.asStateFlow()

    init {
        scope.launch {}
    }

    data class QueryInfoState(
        val stations : List<String> = emptyList(),
        val trainTypes: List<String> = emptyList(),
        val serviceClasses: List<String> = emptyList()
    )
}