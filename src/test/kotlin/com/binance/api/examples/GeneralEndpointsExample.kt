package com.binance.api.examples

import com.binance.api.client.BinanceApiClientFactory
import com.binance.api.client.domain.general.Asset
import com.binance.api.client.domain.general.FilterType

/**
 * Examples on how to use the general endpoints.
 */
object GeneralEndpointsExample {
    @JvmStatic
    fun main(args: Array<String>) {
        val factory = BinanceApiClientFactory.newInstance()
        val client = factory.newRestClient()

        // Test connectivity
        client.ping()

        // Check server time
        val serverTime: Long = client.serverTime!!
        println(serverTime)

        // Exchange info
        val exchangeInfo = client.exchangeInfo
        println(exchangeInfo!!.timezone)
        println(exchangeInfo.symbols)

        // Obtain symbol information
        val symbolInfo = exchangeInfo.getSymbolInfo("ETHBTC")
        println(symbolInfo.status)

        val priceFilter = symbolInfo.getSymbolFilter(FilterType.PRICE_FILTER)
        println(priceFilter!!.minPrice)
        println(priceFilter.tickSize)

        // Obtain asset information
        val allAssets = client.allAssets
        println(allAssets!!.stream().filter { asset: Asset? -> asset!!.assetCode == "BNB" }.findFirst().get())

        // Obtain market cap info
        val marketCap = client.getMarketCap("ETHBTC")
        println(marketCap)
    }
}