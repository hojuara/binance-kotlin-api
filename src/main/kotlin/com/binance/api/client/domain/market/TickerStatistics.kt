package com.binance.api.client.domain.market

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * 24 hour price change statistics for a ticker.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class TickerStatistics(
    /** Ticker symbol. */
    val symbol: String,

    /** Price change during the last 24 hours. */
    val priceChange: String,

    /** Price change, in percentage, during the last 24 hours. */
    val priceChangePercent: String,

    /** Weighted average price. */
    val weightedAvgPrice: String,

    /** Previous close price. */
    val prevClosePrice: String,

    /** Last price. */
    val lastPrice: String,

    /** Last qty. */
    val lastQty: String,

    /** Bid price. */
    val bidPrice: String,

    /** Bid qty. */
    val bidQty: String,

    /** Ask price. */
    val askPrice: String,

    /** Ask qty. */
    val askQty: String,

    /** Open price 24 hours ago. */
    val openPrice: String,

    /** Highest price during the past 24 hours. */
    val highPrice: String,

    /** Lowest price during the past 24 hours. */
    val lowPrice: String,

    /** Total volume during the past 24 hours. */
    val volume: String,

    /** Total volume in quote asset during the past 24 hours. */
    val quoteVolume: String,

    /** Open time. */
    val openTime: Long,

    /** Close time. */
    val closeTime: Long,

    /** First trade id. */
    val firstId: Long,

    /** Last trade id. */
    val lastId: Long,

    /** Total number of trades during the last 24 hours. */
    val count: Long
)