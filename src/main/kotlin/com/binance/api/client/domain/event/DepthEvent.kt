package com.binance.api.client.domain.event

import com.binance.api.client.domain.market.OrderBookEntry
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Depth delta event for a symbol.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class DepthEvent(
    @get:JsonProperty("e")
    @field:JsonProperty("e")
    var eventType: String? = null,

    @get:JsonProperty("E")
    @field:JsonProperty("E")
    var eventTime: Long = 0,

    @get:JsonProperty("s")
    @field:JsonProperty("s")
    var symbol: String? = null,

    @get:JsonProperty("U")
    @field:JsonProperty("U")
    var firstUpdateId: Long = 0,

    /**
     * updateId to sync up with updateid in /api/v1/depth
     */
    @get:JsonProperty("u")
    @field:JsonProperty("u")
    var finalUpdateId: Long = 0,

    /**
     * Bid depth delta.
     */
    @get:JsonProperty("b")
    @field:JsonProperty("b")
    var bids: List<OrderBookEntry> = mutableListOf(),

    /**
     * Ask depth delta.
     */
    @get:JsonProperty("a")
    @field:JsonProperty("a")
    var asks: List<OrderBookEntry> = mutableListOf()
) {

    /**
     * @deprecated Use [finalUpdateId]
     */
    @get:Deprecated("Use finalUpdateId", ReplaceWith("finalUpdateId"))
    @set:Deprecated("Use finalUpdateId", ReplaceWith("finalUpdateId"))
    var updateId: Long
        get() = finalUpdateId
        set(value) {
            finalUpdateId = value
        }
}