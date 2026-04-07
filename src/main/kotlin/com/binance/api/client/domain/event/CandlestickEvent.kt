package com.binance.api.client.domain.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize

/**
 * An interval candlestick for a symbol providing informations on price that can
 * be used to produce candlestick charts.
 */
@JsonDeserialize(using = CandlestickEventDeserializer::class)
@JsonSerialize(using = CandlestickEventSerializer::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class CandlestickEvent(
    val eventType: String,
    val eventTime: Long,
    val symbol: String,
    val openTime: Long,
    val open: String,
    val high: String,
    val low: String,
    val close: String,
    val volume: String,
    val closeTime: Long,
    val intervalId: String,
    val firstTradeId: Long,
    val lastTradeId: Long,
    val quoteAssetVolume: String,
    val numberOfTrades: Long,
    val takerBuyBaseAssetVolume: String,
    val takerBuyQuoteAssetVolume: String,
    val isBarFinal: Boolean
)