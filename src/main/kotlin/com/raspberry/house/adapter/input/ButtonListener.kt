package com.raspberry.house.adapter.input

import com.diozero.api.DebouncedDigitalInputDevice
import com.raspberry.house.client.LedClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.springframework.stereotype.Component
import kotlinx.coroutines.launch

@Component
class ButtonListener (
    private val ledClient: LedClient
){

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun adicionarListener(button: DebouncedDigitalInputDevice, ledAssociado: String) {
        button.addListener { event ->
            if (event.isActive) {
                scope.launch {
                    val startTime = System.currentTimeMillis()
                    println("Button throttle ${button.name}")
                    try {
                        ledClient.sendCommand(ledAssociado)
                        val endTime = System.currentTimeMillis()
                        println("Tempo total para acender o LED: ${endTime - startTime}ms")
                    } catch (e: Exception) {
                        println("Falha ao processar comando do botão: ${e.message}")
                    }
                }
            }
        }
    }

    fun fecharListener(button: DebouncedDigitalInputDevice) {
        button.close()
    }

}
