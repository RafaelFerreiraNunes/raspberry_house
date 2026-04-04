package com.raspberry.house.client

interface LedClient {
    suspend fun sendCommand(ledId: String)
}