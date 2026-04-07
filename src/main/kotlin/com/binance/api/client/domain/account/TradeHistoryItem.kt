package com.binance.api.client.domain.account

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents an executed trade history item.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class TradeHistoryItem(
    /**
     * Trade id.
     */
    val id: Long,

    /**
     * Price.
     */
    val price: String,

    /**
     * Quantity.
     */
    val qty: String,

    /**
     * Trade execution time.
     */
    val time: Long,

    /**
     * Is buyer maker ?
     */
    @field:JsonProperty("isBuyerMaker")
    @get:JsonProperty("isBuyerMaker")
    val isBuyerMaker: Boolean,

    /**
     * Is best match ?
     */
    @field:JsonProperty("isBestMatch")
    @get:JsonProperty("isBestMatch")
    val isBestMatch: Boolean
)