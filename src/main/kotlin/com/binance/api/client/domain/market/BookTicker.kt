package com.binance.api.client.domain.market

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Represents the best price/qty on the order book for a given symbol.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class BookTicker(
    /**
     * Ticker symbol.
     */
    val symbol: String,

    /**
     * Bid price.
     */
    val bidPrice: String,

    /**
     * Bid quantity
     */
    val bidQty: String,

    /**
     * Ask price.
     */
    val askPrice: String,

    /**
     * Ask quantity.
     */
    val askQty: String
)