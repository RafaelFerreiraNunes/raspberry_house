package com.raspberry.house.adapter.input

import com.pi4j.io.gpio.digital.DigitalInput
import com.pi4j.io.gpio.digital.DigitalStateChangeListener
import com.raspberry.house.client.LedClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component

@Component
class ButtonListener(
    private val ledClient: LedClient
) {

    // Mantemos o escopo para IO, garantindo que a chamada gRPC seja assíncrona
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun adicionarListener(button: DigitalInput, ledAssociado: String) {
        button.addListener(DigitalStateChangeListener { event ->
            // No Pi4J com PULL_UP, o botão pressionado gera um estado LOW
            if (event.state().isLow) {
                scope.launch {
                    val startTime = System.currentTimeMillis()

                    // No Pi4J v2 usamos .id() ou .name() dependendo do que você setou na Factory
                    println("Button disparado: ${button.id()} (Pino: ${button.address()})")

                    try {
                        ledClient.sendCommand(ledAssociado)
                        val endTime = System.currentTimeMillis()
                        println("Tempo total para acender o LED via gRPC: ${endTime - startTime}ms")
                    } catch (e: Exception) {
                        println("Falha ao processar comando do botão $ledAssociado: ${e.message}")
                    }
                }
            }
        })
    }

    fun fecharListener(button: DigitalInput) {
        println("Ouvinte do pino ${button.address()} será encerrado pelo shutdown do contexto.")
    }
}
