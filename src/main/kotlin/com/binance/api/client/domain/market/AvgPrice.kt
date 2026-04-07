package com.binance.api.client.domain.market

/**
 * Current average price for a symbol.
 */
data class AvgPrice(
    /**
     * Average price interval (in minutes)
     */
    val mins: Int,

    /**
     * Average price
     */
    val price: String,

    /**
     * ast trade time
     */
    val closeTime: Long
)
