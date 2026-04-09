package com.binance.api.client.domain.event

import com.binance.api.client.domain.market.AggTrade
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * An aggregated trade event for a symbol.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class AggTradeEvent(
    override val aggregatedTradeId: Long,
    override val price: String,
    override val quantity: String,
    override val firstBreakdownTradeId: Long,
    override val lastBreakdownTradeId: Long,
    override val tradeTime: Long,
    override val isBuyerMaker: Boolean,
    override val wasBestPriceMatch: Boolean
) : AggTrade(
    aggregatedTradeId = aggregatedTradeId,
    price = price,
    quantity = quantity,
    firstBreakdownTradeId = firstBreakdownTradeId,
    lastBreakdownTradeId = lastBreakdownTradeId,
    tradeTime = tradeTime,
    isBuyerMaker = isBuyerMaker,
    wasBestPriceMatch = wasBestPriceMatch
)