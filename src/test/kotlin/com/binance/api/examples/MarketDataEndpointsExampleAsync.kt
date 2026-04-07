package com.binance.api.examples

import com.binance.api.client.BinanceApiCallback
import com.binance.api.client.BinanceApiClientFactory
import com.binance.api.client.domain.market.*
import com.binance.api.client.exception.BinanceApiException

/**
 * Examples on how to get market data information such as the latest price of a
 * symbol, etc., in an async way.
 */
object MarketDataEndpointsExampleAsync {
    @JvmStatic
    fun main(args: Array<String>) {
        val factory = BinanceApiClientFactory.newInstance()
        val client = factory.newAsyncRestClient()

        // Getting depth of a symbol (async)
        client.getOrderBook("NEOETH", 10, BinanceApiCallback { response: OrderBook? ->
            println(response!!.bids)
        })

        // Getting latest price of a symbol (async)
        client.get24HrPriceStatistics("NEOETH", BinanceApiCallback { response: TickerStatistics? ->
            println(response)
        })

        // Getting all latest prices (async)
        client.getAllPrices(BinanceApiCallback { response ->
            println(response)
        })

        // Getting agg trades (async)
        client.getAggTrades("NEOETH", BinanceApiCallback { response -> println(response) })

        // Weekly candlestick bars for a symbol
        client.getCandlestickBars(
            "NEOETH", CandlestickInterval.WEEKLY,
            BinanceApiCallback { response -> println(response) })

        // Book tickers (async)
        client.getBookTickers(BinanceApiCallback { response -> println(response) })

        // Exception handling
        try {
            client.getOrderBook("UNKNOWN", 10, BinanceApiCallback { response: OrderBook? -> println(response) })
        } catch (e: BinanceApiException) {
            println(e.error!!.code) // -1121
            println(e.error!!.msg) // Invalid symbol
        }
    }
}