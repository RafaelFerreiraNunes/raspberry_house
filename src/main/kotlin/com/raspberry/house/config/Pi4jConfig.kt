package com.raspberry.house.config

import com.pi4j.Pi4J
import com.pi4j.context.Context
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Pi4jConfig {

    @Bean(destroyMethod = "shutdown")
    fun pi4jContext(): Context {
        return Pi4J.newAutoContext()
    }
}