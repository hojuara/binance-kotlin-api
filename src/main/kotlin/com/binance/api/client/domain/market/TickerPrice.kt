package com.binance.api.client.domain.market

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Wraps a symbol and its corresponding latest price.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class TickerPrice(
    /**
     * Ticker symbol.
     */
    var symbol: String? = null,

    /**
     * Latest price.
     */
    var price: String? = null
)