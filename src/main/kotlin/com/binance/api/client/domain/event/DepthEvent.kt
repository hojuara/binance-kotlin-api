package com.binance.api.client.domain.event

import com.binance.api.client.domain.market.OrderBookEntry
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Depth delta event for a symbol.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class DepthEvent(
    val lastUpdateId: Long,
    val bids: List<OrderBookEntry>,
    val asks: List<OrderBookEntry>
)