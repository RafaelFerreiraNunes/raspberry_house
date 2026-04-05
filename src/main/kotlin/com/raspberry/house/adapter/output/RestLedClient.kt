package com.raspberry.house.adapter.output

import com.raspberry.house.client.LedClient
import io.github.resilience4j.kotlin.retry.executeSuspendFunction
import io.github.resilience4j.retry.RetryRegistry
import kotlinx.coroutines.future.await
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture

@Service
@ConditionalOnProperty(name = ["app.communication.type"], havingValue = "rest")
class RestLedClient(
    private val retryRegistry: RetryRegistry
) : LedClient {

    private val httpClient = HttpClient.newHttpClient()

    @Value("\${app.led.url:http://localhost:8090}")
    lateinit var ledBaseUrl: String

    private val retry = retryRegistry.retry("ledService")


    override suspend fun sendCommand(ledId: String) {

        val url = "$ledBaseUrl/led/$ledId"

        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .build()

        try {
            retry.executeSuspendFunction {
                // .await() transforma o CompletableFuture em uma suspensão do Kotlin
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).await()
                println("REST: Comando enviado para $url")
            }
        } catch (e: Exception) {
            println("Erro persistente ao enviar REST: ${e.message}")
            throw e // Precisamos relançar para o Resilience4j saber que falhou
        }
    }

    fun sendCommandCompletableFuture(posLed: String): CompletableFuture<Void> {
        val request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8090/led/$posLed")).GET().build()

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept { }
    }
}
