package com.app

import com.app.client.InsuranceCalculationWsClient
import io.micronaut.context.annotation.Prototype
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.discovery.event.ServiceReadyEvent
import io.micronaut.http.client.annotation.Client
import io.micronaut.websocket.WebSocketClient
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import reactor.core.Disposable
import reactor.core.publisher.Flux

@Singleton
open class ClientLoadTesting(@Client("http://10.129.0.29:8080") val webSocketClient: WebSocketClient) : ApplicationEventListener<ServiceReadyEvent> {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun onApplicationEvent(event: ServiceReadyEvent?) {
        val clients = mutableListOf<Disposable?>()
        var currentNumber = 0L

        for (i in 1..180) {
            for (j in 1..15_000) {
                val client = Flux.from(webSocketClient.connect(InsuranceCalculationWsClient::class.java, "/ws/failing/calculation")).subscribe { client -> client.send("") }

                clients.add(client)
                ++currentNumber
            }
            log.info("Current clients: $currentNumber")
            Thread.sleep(1_000)
        }
    }

}
