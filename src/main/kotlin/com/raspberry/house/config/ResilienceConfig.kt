package com.raspberry.house.config

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.retry.RetryConfig
import io.github.resilience4j.retry.RetryRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class ResilienceConfig {

    @Bean
    fun retryRegistry(): RetryRegistry {
        val config = RetryConfig.custom<Any>()
            .maxAttempts(3)
            .waitDuration(Duration.ofMillis(1000))
            .retryExceptions(
                io.grpc.StatusRuntimeException::class.java,
                io.grpc.StatusException::class.java,
                java.io.IOException::class.java // O gRPC costuma embrulhar erros de rede aqui
            )
            .build()

        val registry = RetryRegistry.of(config)

        // Escutador global para todos os retries criados por este registry
        registry.retry("ledService").eventPublisher.onRetry { event ->
            println("--- [Resilience4j] Tentativa de Retry nº ${event.numberOfRetryAttempts} devido a: ${event.lastThrowable.message}")
        }

        return registry
    }

    @Bean
    fun circuitBreakerRegistry(): CircuitBreakerRegistry {
        val config = CircuitBreakerConfig.custom()
            .failureRateThreshold(50f)
            .waitDurationInOpenState(Duration.ofSeconds(10))
            .slidingWindowSize(10)
            .build()

        val registry = CircuitBreakerRegistry.of(config)

        // Escutador para ver o Circuito mudando de estado
        registry.circuitBreaker("ledService").eventPublisher.onStateTransition { event ->
            println("--- [Resilience4j] Circuito mudou de ${event.stateTransition.fromState} para ${event.stateTransition.toState}")
        }

        return registry
    }
}