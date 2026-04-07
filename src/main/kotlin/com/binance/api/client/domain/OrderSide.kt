package com.binance.api.client.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Buy/Sell order side.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
enum class OrderSide {
    BUY,
    SELL
}