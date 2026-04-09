package com.binance.api.client.domain.event

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape.ARRAY
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonPropertyOrder

/**
 * An interval candlestick for a symbol providing informations on price that can
 * be used to produce candlestick charts.
 */
@JsonFormat(shape = ARRAY)
@JsonPropertyOrder("openTime", "open", "high", "low", "close", "volume", "closeTime",
    "quoteAssetVolume", "numberOfTrades", "takerBuyBaseAssetVolume", "takerBuyQuoteAssetVolume")
@JsonIgnoreProperties(ignoreUnknown = true)
data class CandlestickEvent(
    val openTime: Long,
    val open: String,
    val high: String,
    val low: String,
    val close: String,
    val volume: String,
    val closeTime: Long,
    val quoteAssetVolume: String,
    val numberOfTrades: Long,
    val takerBuyBaseAssetVolume: String,
    val takerBuyQuoteAssetVolume: String
)