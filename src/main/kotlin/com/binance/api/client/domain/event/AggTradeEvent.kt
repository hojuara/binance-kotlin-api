package com.binance.api.client.domain.event

import com.binance.api.client.domain.market.AggTrade
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * An aggregated trade event for a symbol.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class AggTradeEvent(

    @get:JsonProperty("e")
    @field:JsonProperty("e")
    val eventType: String,

    @get:JsonProperty("E")
    @field:JsonProperty("E")
    val eventTime: Long,

    @get:JsonProperty("s")
    @field:JsonProperty("s")
    val symbol: String,

    override val aggregatedTradeId: Long,
    override val price: String,
    override val quantity: String,
    override val firstBreakdownTradeId: Long,
    override val lastBreakdownTradeId: Long,
    override val tradeTime: Long,
    override val isBuyerMaker: Boolean
) : AggTrade(
    aggregatedTradeId = aggregatedTradeId,
    price = price,
    quantity = quantity,
    firstBreakdownTradeId = firstBreakdownTradeId,
    lastBreakdownTradeId = lastBreakdownTradeId,
    tradeTime = tradeTime,
    isBuyerMaker = isBuyerMaker
)