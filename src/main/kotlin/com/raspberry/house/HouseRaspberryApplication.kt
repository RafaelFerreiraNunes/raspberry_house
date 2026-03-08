package com.raspberry.house

import com.raspberry.house.adapter.input.ButtonFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HouseRaspberryApplication

fun main(args: Array<String>) {
    runApplication<HouseRaspberryApplication>(*args)

    val b17 = ButtonFactory.createButton(17)

    b17.addListener { event ->
        if (event.isActive) {
            println("Button active")
        }
    }

    readln()
    b17.close()

}
