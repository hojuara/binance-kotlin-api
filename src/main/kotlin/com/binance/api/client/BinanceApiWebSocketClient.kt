package com.binance.api.client

import com.binance.api.client.domain.event.*
import com.binance.api.client.domain.market.CandlestickInterval
import java.io.Closeable

/**
 * Binance API data streaming façade, supporting streaming of events through web sockets.
 */
interface BinanceApiWebSocketClient {

    /**
     * Open a new web socket to receive [depthEvents][DepthEvent] on a callback.
     *
     * @param symbol   market symbol to subscribe to
     * @param callback  the callback to call on new events
     * @return a [Closeable] that allows the underlying web socket to be closed.
     */
    fun onDepthEvent(symbol: String, callback: BinanceApiCallback<DepthEvent>): Closeable

    /**
     * Open a new web socket to receive [candlestickEvents][CandlestickEvent] on a callback.
     *
     * @param symbol   market symbol to subscribe to
     * @param interval  the interval of the candles tick events required
     * @param callback  the callback to call on new events
     * @return a [Closeable] that allows the underlying web socket to be closed.
     */
    fun onCandlestickEvent(
        symbol: String,
        interval: CandlestickInterval,
        callback: BinanceApiCallback<Array<CandlestickEvent>>
    ): Closeable

    /**
     * Open a new web socket to receive [aggTradeEvents][AggTradeEvent] on a callback.
     *
     * @param symbol   market symbol to subscribe to
     * @param callback  the callback to call on new events
     * @return a [Closeable] that allows the underlying web socket to be closed.
     */
    fun onAggTradeEvent(symbol: String, callback: BinanceApiCallback<Array<AggTradeEvent>>): Closeable

    /**
     * Open a new web socket to receive [userDataUpdateEvents][UserDataUpdateEvent] on a callback.
     *
     * @param callback  the callback to call on new events
     * @return a [Closeable] that allows the underlying web socket to be closed.
     */
    fun onUserDataUpdateEvent(callback: BinanceApiCallback<UserDataUpdateEvent>): Closeable

    /**
     * Open a new web socket to receive {@linke BookTickerEvent bookTickerEvents} on a callback.
     *
     * @param symbols    market symbols to subscribe to
     * @param callback  the callback to call on new events
     * @return
     */
    fun onBookTickerEvent(symbols: Array<String>, callback: BinanceApiCallback<Array<BookTickerEvent>>): Closeable
}