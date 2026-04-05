package com.raspberry.house.adapter.input

import com.pi4j.context.Context
import com.pi4j.io.gpio.digital.DigitalInput
import com.pi4j.io.gpio.digital.PullResistance
import org.springframework.stereotype.Component

@Component
class ButtonFactory(private val pi4jContext: Context) {

    fun createButton(pinAddress: Int): DigitalInput {
        val config = DigitalInput.newConfigBuilder(pi4jContext)
            .id("btn-pino-$pinAddress")
            .address(pinAddress)
            .pull(PullResistance.PULL_UP) // Botão geralmente liga o pino ao GND (Terra)
            .debounce(10000L)            // 10ms para evitar ruído elétrico (ghost clicks)
            .provider("gpiod-digital-input") // Driver correto para o Pi 5
            .build()

        return pi4jContext.create(config)
    }
}