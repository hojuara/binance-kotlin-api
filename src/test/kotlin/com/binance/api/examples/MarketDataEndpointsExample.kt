package com.binance.api.examples

import com.binance.api.client.BinanceApiClientFactory
import com.binance.api.client.domain.market.CandlestickInterval
import com.binance.api.client.exception.BinanceApiException

/**
 * Examples on how to get market data information such as the latest price of a
 * symbol, etc.
 */
object MarketDataEndpointsExample {
    @JvmStatic
    fun main(args: Array<String>) {
        val factory = BinanceApiClientFactory.newInstance()
        val client = factory.newRestClient()

        // Getting depth of a symbol
        val orderBook = client.getOrderBook("NEOETH", 10)
        println(orderBook!!.asks)

        // Getting latest price of a symbol
        val tickerStatistics = client.get24HrPriceStatistics("NEOETH")
        println(tickerStatistics)

        // Getting all latest prices
        val allPrices = client.allPrices
        println(allPrices)

        // Getting agg trades
        val aggTrades = client.getAggTrades("NEOETH")
        println(aggTrades)

        // Weekly candlestick bars for a symbol
        val candlesticks = client.getCandlestickBars("NEOETH", CandlestickInterval.WEEKLY)
        println(candlesticks)

        // Getting all book tickers
        val allBookTickers = client.bookTickers
        println(allBookTickers)

        // Exception handling
        try {
            client.getOrderBook("UNKNOWN", 10)
        } catch (e: BinanceApiException) {
            println(e.error!!.code) // -1121
            println(e.error!!.msg) // Invalid symbol
        }
    }
}