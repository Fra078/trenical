package it.trenical.customer.gui.ui.viewModels

import androidx.compose.runtime.mutableStateListOf
import it.trenical.customer.gui.data.NotificationManager
import it.trenical.customer.gui.data.models.Notification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NotificationViewModel(manager: NotificationManager) {
    private val scope = CoroutineScope(Dispatchers.IO)
    val list = mutableStateListOf<Notification>()


    init {
       manager.channel.onEach { notification ->
           list.add(notification)
       }.launchIn(scope)
    }

}