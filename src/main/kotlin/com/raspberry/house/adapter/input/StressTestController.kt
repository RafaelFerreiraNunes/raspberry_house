package com.raspberry.house.adapter.input

import com.raspberry.house.client.LedClient
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/internal/test")
class StressTestController(
    private val ledClient: LedClient // Injeta o GrpcLedClient ou RestLedClient automaticamente
) {

    private val log = LoggerFactory.getLogger(javaClass) // Declare o logger

    @GetMapping("/stress/{ledId}/{count}")
    suspend fun runStressTest(
        @PathVariable ledId: String,
        @PathVariable count: Int
    ): Map<String, Any> {
        log.info("--- Iniciando Stress Test: $count chamadas para o LED $ledId")

        val latencies = mutableListOf<Double>()
        var successes = 0
        var failures = 0

        repeat(count) { i ->
            val start = System.nanoTime()
            try {
                ledClient.sendCommand(ledId)
                val end = System.nanoTime()
                latencies.add((end - start) / 1_000_000.0)
                successes++
            } catch (e: Exception) {
                log.error("Erro na chamada {} para o LED {}: {}", i + 1, ledId, e.message)
                failures++
            }
        }

        val avgLatency = if (latencies.isNotEmpty()) latencies.average() else 0.0

        val result = mapOf(
            "total_calls" to count,
            "successes" to successes,
            "failures" to failures,
            "avg_latency_ms" to "%.2f".format(avgLatency),
            "max_latency_ms" to "%.2f".format(latencies.maxOrNull() ?: 0.0),
            "min_latency_ms" to "%.2f".format(latencies.minOrNull() ?: 0.0)
        )

        log.info("--- Stress Test Finalizado: {}", result)

        return result
    }
}
