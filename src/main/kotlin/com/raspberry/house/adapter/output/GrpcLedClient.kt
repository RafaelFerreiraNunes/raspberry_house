package com.raspberry.house.adapter.output

import com.raspberry.house.client.LedClient
import com.raspberry.house_raspberry_out.grpc.LedRequest
import com.raspberry.house_raspberry_out.grpc.LedServiceGrpcKt
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(name = ["app.communication.type"], havingValue = "grpc")
class GrpcLedClient(
    @GrpcClient("ledServer")
    private val stub: LedServiceGrpcKt.LedServiceCoroutineStub
) : LedClient {

    override suspend fun sendCommand(ledId: String) {
        try {
            // Usando a DSL do Kotlin (protobuf-kotlin) para criar a mensagem
            val request = LedRequest.newBuilder()
                .setLedId(ledId.toInt())
                .build()

            val response = stub.changeStatus(request)
            println("gRPC: Resposta do servidor -> ${response.message}")
        } catch (e: Exception) {
            println("Falha na chamada gRPC: ${e.message}")
        }
    }
}