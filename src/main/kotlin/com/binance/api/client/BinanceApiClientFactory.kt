package com.binance.api.client

import com.binance.api.client.impl.BinanceApiAsyncRestClientImpl
import com.binance.api.client.impl.BinanceApiRestClientImpl
import com.binance.api.client.impl.BinanceApiWebSocketClientImpl

/**
 * A factory for creating BinanceApi client objects.
 */
class BinanceApiClientFactory
/**
 * Instantiates a new binance api client factory.
 *
 * @param apiKey the API key
 * @param secret the Secret
 */ private constructor(
    /**
     * API Key
     */
    private val apiKey: String?,
    /**
     * Secret.
     */
    private val secret: String?
) {
    /**
     * Creates a new synchronous/blocking REST client.
     */
    fun newRestClient(): BinanceApiRestClient {
        return BinanceApiRestClientImpl(apiKey, secret)
    }

    /**
     * Creates a new asynchronous/non-blocking REST client.
     */
    fun newAsyncRestClient(): BinanceApiAsyncRestClient {
        return BinanceApiAsyncRestClientImpl(apiKey, secret)
    }

    /**
     * Creates a new web socket client used for handling data streams.
     */
    fun newWebSocketClient(): BinanceApiWebSocketClient {
        return BinanceApiWebSocketClientImpl(apiKey, secret)
    }

    companion object {
        /**
         * New instance.
         *
         * @param apiKey the API key
         * @param secret the Secret
         *
         * @return the binance api client factory
         */
        fun newInstance(apiKey: String, secret: String): BinanceApiClientFactory {
            return BinanceApiClientFactory(apiKey, secret)
        }

        /**
         * New instance without authentication.
         *
         * @return the binance api client factory
         */
        fun newInstance(): BinanceApiClientFactory {
            return BinanceApiClientFactory(null, null)
        }
    }
}