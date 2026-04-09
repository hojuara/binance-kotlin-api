package com.binance.api.client.domain.market

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * An aggregated trade event for a symbol.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
open class AggTrade(
    @field:JsonProperty("a")
    @get:JsonProperty("a")
    open val aggregatedTradeId: Long,

    @field:JsonProperty("p")
    @get:JsonProperty("p")
    open val price: String,

    @field:JsonProperty("q")
    @get:JsonProperty("q")
    open val quantity: String,

    @field:JsonProperty("f")
    @get:JsonProperty("f")
    open val firstBreakdownTradeId: Long,

    @field:JsonProperty("l")
    @get:JsonProperty("l")
    open val lastBreakdownTradeId: Long,

    @field:JsonProperty("T")
    @get:JsonProperty("T")
    open val tradeTime: Long,

    @field:JsonProperty("m")
    @get:JsonProperty("m")
    open val isBuyerMaker: Boolean,

    @field:JsonProperty("M")
    @get:JsonProperty("M")
    open val wasBestPriceMatch: Boolean
)