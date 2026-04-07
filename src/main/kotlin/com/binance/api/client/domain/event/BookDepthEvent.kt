package com.binance.api.client.domain.event

import com.binance.api.client.domain.market.OrderBookEntry
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class BookDepthEvent(
    @field:JsonProperty("lastUpdateId")
    @get:JsonProperty("lastUpdateId")
    var lastUpdateId: Long = 0,

    @field:JsonProperty("bids")
    @get:JsonProperty("bids")
    var bids: List<OrderBookEntry> = mutableListOf(),

    @field:JsonProperty("asks")
    @get:JsonProperty("asks")
    var asks: List<OrderBookEntry> = mutableListOf()
)