package com.binance.api.examples

import com.binance.api.client.BinanceApiCallback
import com.binance.api.client.BinanceApiClientFactory
import com.binance.api.client.domain.general.Asset
import com.binance.api.client.domain.general.ExchangeInfo
import com.binance.api.client.domain.general.FilterType
import com.binance.api.client.domain.general.ServerTime

/**
 * Examples on how to use the general endpoints.
 */
object GeneralEndpointsExampleAsync {
    @Throws(InterruptedException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val factory = BinanceApiClientFactory.newInstance()
        val client = factory.newAsyncRestClient()

        // Test connectivity
        client.ping(BinanceApiCallback { response: Void? -> println("Ping succeeded.") })

        // Check server time
        client.getServerTime(BinanceApiCallback { response: ServerTime? -> println(response!!.serverTime) })

        // Exchange info
        client.getExchangeInfo(BinanceApiCallback { exchangeInfo: ExchangeInfo? ->
            println(exchangeInfo!!.timezone)
            println(exchangeInfo.symbols)

            // Obtain symbol information
            val symbolInfo = exchangeInfo.getSymbolInfo("ETHBTC")
            println(symbolInfo.status)

            val priceFilter = symbolInfo.getSymbolFilter(FilterType.PRICE_FILTER)
            println(priceFilter!!.minPrice)
            println(priceFilter.tickSize)
        })

        // Obtain asset information
        client.getAllAssets(BinanceApiCallback { allAssets ->
            println(
                allAssets.stream().filter { asset: Asset? -> asset!!.assetCode == "BNB" }.findFirst().get()
            )
        })
    }
}