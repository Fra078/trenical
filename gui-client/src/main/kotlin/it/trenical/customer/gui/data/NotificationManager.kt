package it.trenical.customer.gui.data

import it.trenical.customer.gui.data.models.Notification
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object NotificationManager {

    private val _channel = MutableSharedFlow<Notification>(
        extraBufferCapacity = 5
    )
    val channel = _channel.asSharedFlow()

    fun emit(title: String, content: String) {
        println("emitting notif")
        val done = _channel.tryEmit(Notification(title, content))
        println("done: $done")
    }

}