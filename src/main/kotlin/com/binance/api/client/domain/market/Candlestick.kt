package com.binance.api.client.domain.market

import com.binance.api.client.constant.BinanceApiConstants
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import org.apache.commons.lang3.builder.ToStringBuilder

/**
 * Kline/Candlestick bars for a symbol. Klines are uniquely identified by their
 * open time.
 */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder(
    "openTime",
    "open",
    "high",
    "low",
    "close",
    "volume",
    "closeTime",
    "quoteAssetVolume",
    "numberOfTrades",
    "takerBuyBaseAssetVolume",
    "takerBuyQuoteAssetVolume"
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class Candlestick(
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
) {

    override fun toString(): String {
        return ToStringBuilder(this, BinanceApiConstants.TO_STRING_BUILDER_STYLE)
            .append("openTime", openTime)
            .append("open", open)
            .append("high", high)
            .append("low", low)
            .append("close", close)
            .append("volume", volume)
            .append("closeTime", closeTime)
            .append("quoteAssetVolume", quoteAssetVolume)
            .append("numberOfTrades", numberOfTrades)
            .append("takerBuyBaseAssetVolume", takerBuyBaseAssetVolume)
            .append("takerBuyQuoteAssetVolume", takerBuyQuoteAssetVolume)
            .toString()
    }
}