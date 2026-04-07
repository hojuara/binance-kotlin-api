package com.binance.api.client.integration

import com.binance.api.client.BinanceApiClientFactory
import com.binance.api.client.BinanceApiRestClient
import com.binance.api.client.BinanceApiWebSocketClient
import org.junit.jupiter.api.Assumptions.assumeTrue

object BinanceIntegrationTestSupport {
    private const val ENV_API_KEY = "API_KEY"
    private const val ENV_SECRET_KEY = "SECRET_KEY"

    fun requireApiKeysOrSkip() {
        val apiKey = System.getenv(ENV_API_KEY)
        val secretKey = System.getenv(ENV_SECRET_KEY)
        assumeTrue(!apiKey.isNullOrBlank() && !secretKey.isNullOrBlank()) {
            "Skipping Binance integration tests: env vars $ENV_API_KEY and/or $ENV_SECRET_KEY are missing."
        }
    }

    fun factoryFromEnv(): BinanceApiClientFactory {
        val apiKey = System.getenv(ENV_API_KEY)!!.trim()
        val secretKey = System.getenv(ENV_SECRET_KEY)!!.trim()
        return BinanceApiClientFactory.newInstance(apiKey, secretKey)
    }

    fun newRestClient(): BinanceApiRestClient = factoryFromEnv().newRestClient()

    fun newWebSocketClient(): BinanceApiWebSocketClient = factoryFromEnv().newWebSocketClient()
}

