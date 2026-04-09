package com.binance.api.examples

import com.binance.api.client.BinanceApiClientFactory
import com.binance.api.client.domain.event.CandlestickEvent
import com.binance.api.client.domain.market.Candlestick
import com.binance.api.client.domain.market.CandlestickInterval
import java.util.*

/**
 * Illustrates how to use the klines/candlesticks event stream to create a local
 * cache of bids/asks for a symbol.
 */
class CandlesticksCacheExample(symbol: String, interval: CandlestickInterval) {
    /**
     * Key is the start/open time of the candle, and the value contains candlestick
     * date.
     */
    private var candlesticksCache: MutableMap<Long?, Candlestick?>? = null

    init {
        initializeCandlestickCache(symbol, interval)
        startCandlestickEventStreaming(symbol, interval)
    }

    /**
     * Initializes the candlestick cache by using the REST API.
     */
    private fun initializeCandlestickCache(symbol: String, interval: CandlestickInterval) {
        val factory = BinanceApiClientFactory.newInstance()
        val client = factory.newRestClient()
        val candlestickBars =
            client.getCandlestickBars(symbol.uppercase(Locale.getDefault()), interval)

        this.candlesticksCache = TreeMap<Long?, Candlestick?>()
        for (candlestickBar in candlestickBars) {
            candlesticksCache!![candlestickBar.openTime] = candlestickBar
        }
    }

    /**
     * Begins streaming of depth events.
     */
    private fun startCandlestickEventStreaming(symbol: String, interval: CandlestickInterval) {
        val factory = BinanceApiClientFactory.newInstance()
        val client = factory.newWebSocketClient()

        client.onCandlestickEvent(
            symbol.lowercase(Locale.getDefault()),
            interval
        ) { response ->
            val kline = response.first()
            val openTime = kline.openTime
            var updateCandlestick = candlesticksCache!![openTime]
            if (updateCandlestick == null) {
                // new candlestick
                updateCandlestick = Candlestick(
                    openTime = kline.openTime,
                    open = kline.open,
                    low = kline.low,
                    high = kline.high,
                    close = kline.close,
                    closeTime = kline.closeTime,
                    volume = kline.volume,
                    numberOfTrades = kline.numberOfTrades,
                    quoteAssetVolume = kline.quoteAssetVolume,
                    takerBuyQuoteAssetVolume = kline.takerBuyQuoteAssetVolume,
                    takerBuyBaseAssetVolume = kline.takerBuyQuoteAssetVolume
                )
            }

            // Store the updated candlestick in the cache
            candlesticksCache!![openTime] = updateCandlestick
            println(updateCandlestick)
        }
    }

    /**
     * @return a klines/candlestick cache, containing the open/start time of the
     * candlestick as the key, and the candlestick data as the value.
     */
    fun getCandlesticksCache(): MutableMap<Long?, Candlestick?> {
        return candlesticksCache!!
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            CandlesticksCacheExample("ETHBTC", CandlestickInterval.ONE_MINUTE)
        }
    }
}