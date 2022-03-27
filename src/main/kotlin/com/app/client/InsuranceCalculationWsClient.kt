package com.app.client

import io.micronaut.websocket.annotation.ClientWebSocket
import io.micronaut.websocket.annotation.OnMessage
import java.util.concurrent.ConcurrentLinkedQueue

@ClientWebSocket("/ws/failing/calculation")
abstract class InsuranceCalculationWsClient: AutoCloseable {
    val replies = ConcurrentLinkedQueue<CalculationResponse>()

    @OnMessage
    fun onMessage(message: CalculationResponse) {
        replies.add(message)
    }

    abstract fun send(message: String)
}
