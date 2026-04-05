package com.raspberry.house.domain

import com.pi4j.io.gpio.digital.DigitalInput
import com.raspberry.house.adapter.input.ButtonFactory
import com.raspberry.house.adapter.input.ButtonListener
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.stereotype.Service

@Service
class LedButtonService(
    private val buttonFactory: ButtonFactory,
    private val buttonListener: ButtonListener
) {

    private val buttonLedMap = mapOf(
        17 to "1",
        27 to "2",
        22 to "3",
        5  to "4",
        6  to "5",
        13 to "6"
    )
    private val buttons = ArrayList<DigitalInput>(6)


    @PostConstruct
    fun init() {
        buttonLedMap.forEach { pin, ledId ->
            val button = buttonFactory.createButton(pin)
            buttons.add(button)
            buttonListener.adicionarListener(button, ledId)
        }
    }

    @PreDestroy
    fun cleanup() {
        println("Encerrando botões e limpando listeners...")
        buttons.forEach { bt ->
            buttonListener.fecharListener(bt)
        }
    }
}