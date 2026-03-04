package com.raspberry.house

import com.raspberry.house.adapter.input.ButtonFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HouseRaspberryApplication

fun main(args: Array<String>) {
    runApplication<HouseRaspberryApplication>(*args)

    val b17 = ButtonFactory.createButton(17)

    try {
        while (true){
            when(b17.value){
                true -> print("Botão pressionado!")
                false -> print("Botão livre!")
            }
            Thread.sleep(400)
        }
    } finally {
        b17.close()
    }

}
