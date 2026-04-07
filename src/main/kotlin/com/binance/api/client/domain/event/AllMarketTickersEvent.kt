package com.binance.api.client.domain.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class AllMarketTickersEvent(
    @field:JsonProperty("e")
    @get:JsonProperty("e")
    var eventType: String? = null,

    @field:JsonProperty("E")
    @get:JsonProperty("E")
    var eventTime: Long = 0,

    @field:JsonProperty("s")
    @get:JsonProperty("s")
    var symbol: String? = null,

    @field:JsonProperty("p")
    @get:JsonProperty("p")
    var priceChange: String? = null,

    @field:JsonProperty("P")
    @get:JsonProperty("P")
    var priceChangePercent: String? = null,

    @field:JsonProperty("w")
    @get:JsonProperty("w")
    var weightedAveragePrice: String? = null,

    @field:JsonProperty("x")
    @get:JsonProperty("x")
    var previousDaysClosePrice: String? = null,

    @field:JsonProperty("c")
    @get:JsonProperty("c")
    var currentDaysClosePrice: String? = null,

    @field:JsonProperty("Q")
    @get:JsonProperty("Q")
    var closeTradesQuantity: String? = null,

    @field:JsonProperty("b")
    @get:JsonProperty("b")
    var bestBidPrice: String? = null,

    @field:JsonProperty("B")
    @get:JsonProperty("B")
    var bestBidQuantity: String? = null,

    @field:JsonProperty("a")
    @get:JsonProperty("a")
    var bestAskPrice: String? = null,

    @field:JsonProperty("A")
    @get:JsonProperty("A")
    var bestAskQuantity: String? = null,

    @field:JsonProperty("o")
    @get:JsonProperty("o")
    var openPrice: String? = null,

    @field:JsonProperty("h")
    @get:JsonProperty("h")
    var highPrice: String? = null,

    @field:JsonProperty("l")
    @get:JsonProperty("l")
    var lowPrice: String? = null,

    @field:JsonProperty("v")
    @get:JsonProperty("v")
    var totalTradedBaseAssetVolume: String? = null,

    @field:JsonProperty("q")
    @get:JsonProperty("q")
    var totalTradedQuoteAssetVolume: String? = null,

    @field:JsonProperty("O")
    @get:JsonProperty("O")
    var statisticesOpenTime: Long = 0,

    @field:JsonProperty("C")
    @get:JsonProperty("C")
    var statisticesCloseTime: Long = 0,

    @field:JsonProperty("F")
    @get:JsonProperty("F")
    var firstTradeId: Long = 0,

    @field:JsonProperty("L")
    @get:JsonProperty("L")
    var lastTradeId: Long = 0,

    @field:JsonProperty("n")
    @get:JsonProperty("n")
    var totalNumberOfTrades: Long = 0
)