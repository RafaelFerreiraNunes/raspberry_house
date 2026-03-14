package com.raspberry.house.adapter.input

import com.diozero.api.DebouncedDigitalInputDevice
import com.raspberry.house.adapter.output.RequestOut
import org.springframework.stereotype.Component

@Component
class ButtonListener {

    fun adicionarListener(button: DebouncedDigitalInputDevice, ledAssociado: String) {
        button.addListener { event ->
            if (event.isActive) {
                val startTime = System.currentTimeMillis()
                println("Button throttle ${button.name}")
                RequestOut.sendCommandLed(ledAssociado).thenRun {
                    val endTime = System.currentTimeMillis()
                    println("Tempo total para acender o LED: ${endTime - startTime}ms")
                }
            }
        }
    }

    fun fecharListener(button: DebouncedDigitalInputDevice) {
        button.close()
    }

}
