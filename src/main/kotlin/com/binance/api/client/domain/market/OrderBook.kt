package com.binance.api.client.domain.market

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Order book of a symbol in Binance.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class OrderBook(
    /**
     * Last update id of this order book.
     */
    val lastUpdateId: Long,

    /**
     * List of bids (price/qty).
     */
    val bids: List<OrderBookEntry>,

    /**
     * List of asks (price/qty).
     */
    val asks: List<OrderBookEntry>
)