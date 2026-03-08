package com.raspberry.house.adapter.output

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object RequestOut {

    private val httpClient = HttpClient.newHttpClient()

    fun sendCommandLed(posLed: String) {
        val request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8090/led/$posLed")).GET().build()

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())

    }
}