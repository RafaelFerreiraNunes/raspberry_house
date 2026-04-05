package com.raspberry.house.adapter.output

import com.raspberry.house.client.LedClient
import com.raspberry.house_raspberry_out.grpc.LedRequest
import com.raspberry.house_raspberry_out.grpc.LedServiceGrpcKt
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.kotlin.retry.executeSuspendFunction
import io.github.resilience4j.retry.RetryRegistry
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import io.github.resilience4j.kotlin.circuitbreaker.executeSuspendFunction

@Service
@ConditionalOnProperty(name = ["app.communication.type"], havingValue = "grpc")
class GrpcLedClient(
    @GrpcClient("ledServer")
    private val stub: LedServiceGrpcKt.LedServiceCoroutineStub,
    private val retryRegistry: RetryRegistry,
    private val circuitBreakerRegistry: CircuitBreakerRegistry
) : LedClient {

    private val retry = retryRegistry.retry("ledService")
    private val circuitBreaker = circuitBreakerRegistry.circuitBreaker("ledService")

    override suspend fun sendCommand(ledId: String) {
        try {
            // Usando a DSL do Kotlin (protobuf-kotlin) para criar a mensagem
            val request = LedRequest.newBuilder()
                .setLedId(ledId.toInt())
                .build()

            circuitBreaker.executeSuspendFunction {
                retry.executeSuspendFunction {
                    println("--- [DEBUG] Tentando chamada gRPC agora...")
                    val response = stub.changeStatus(request)
                    println("gRPC: Resposta do servidor -> ${response.message}")
                }
            }
        } catch (e: Exception) {
            println("Falha na chamada gRPC: ${e.message}")
            throw e // Relançar para que o Resilience4j possa aplicar as políticas de retry/circuit breaker
        }
    }
}