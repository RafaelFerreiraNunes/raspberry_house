package com.raspberry.house.adapter.input

import com.diozero.api.DebouncedDigitalInputDevice
import com.raspberry.house.adapter.output.RequestOut
import org.springframework.stereotype.Component

@Component
class ButtonListener {

    fun adicionarListener(button: DebouncedDigitalInputDevice, ledAssociado: String) {
        button.addListener { event ->
            if (event.isActive) {
                println("Button throttle ${button.name}")
                RequestOut.sendCommandLed(ledAssociado)
            }
        }
    }

    fun fecharListener(button: DebouncedDigitalInputDevice) {
        button.close()
    }

}
