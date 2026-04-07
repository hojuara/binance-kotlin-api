package com.binance.api.examples

import com.binance.api.client.BinanceApiCallback
import com.binance.api.client.BinanceApiClientFactory
import com.binance.api.client.domain.event.AllMarketTickersEvent

/**
 * All market tickers channel examples.
 *
 * It illustrates how to create a stream to obtain all market tickers.
 */
object AllMarketTickersExample {
    @JvmStatic
    fun main(args: Array<String>) {
        val factory = BinanceApiClientFactory.newInstance()
        val client = factory.newWebSocketClient()

        client.onAllMarketTickersEvent(BinanceApiCallback { event ->
            println(event)
        })
    }
}