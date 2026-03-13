package com.raspberry.house

import com.raspberry.house.adapter.input.ButtonFactory
import com.raspberry.house.adapter.output.RequestOut
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HouseRaspberryApplication

fun main(args: Array<String>) {
    runApplication<HouseRaspberryApplication>(*args)

}
