package com.binance.api.examples

import com.binance.api.client.BinanceApiCallback
import com.binance.api.client.BinanceApiClientFactory
import com.binance.api.client.domain.event.AggTradeEvent
import com.binance.api.client.domain.event.CandlestickEvent
import com.binance.api.client.domain.event.DepthEvent
import com.binance.api.client.domain.market.CandlestickInterval
import java.io.IOException

/**
 * Market data stream endpoints examples.
 *
 * It illustrates how to create a stream to obtain updates on market data such
 * as executed trades.
 */
object MarketDataStreamExample {
    @Throws(InterruptedException::class, IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val client = BinanceApiClientFactory.newInstance().newWebSocketClient()

        // Listen for aggregated trade events for ETH/BTC
        client.onAggTradeEvent("ethbtc", BinanceApiCallback { response: AggTradeEvent? -> println(response) })

        // Listen for changes in the order book in ETH/BTC
        client.onDepthEvent("ethbtc", BinanceApiCallback { response: DepthEvent? -> println(response) })

        // Obtain 1m candlesticks in real-time for ETH/BTC
        client.onCandlestickEvent(
            "ethbtc",
            CandlestickInterval.ONE_MINUTE,
            BinanceApiCallback { response: CandlestickEvent? -> println(response) })
    }
}