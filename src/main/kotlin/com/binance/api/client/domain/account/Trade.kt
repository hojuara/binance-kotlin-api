package com.binance.api.client.domain.account

import com.binance.api.client.constant.BinanceApiConstants
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import org.apache.commons.lang3.builder.ToStringBuilder

/**
 * Represents an executed trade.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Trade(
    /**
     * The symbol of the trade.
     */
    val symbol: String,

    /**
     * Trade id.
     */
    val id: Long,

    /**
     * Order id.
     */
    val orderId: Long,

    /**
     * Price.
     */
    val price: String,

    /**
     * Quantity.
     */
    val qty: String,

    /**
     * Quote quantity for the trade (price * qty).
     */
    val quoteQty: String,

    /**
     * Commission.
     */
    val commission: String,

    /**
     * Asset on which commission is taken
     */
    val commissionAsset: String,

    /**
     * Trade execution time.
     */
    val time: Long,

    @get:JsonProperty("isBuyer")
    @field:JsonProperty("isBuyer")
    val isBuyer: Boolean,

    @get:JsonProperty("isMaker")
    @field:JsonProperty("isMaker")
    val isMaker: Boolean,

    @get:JsonProperty("isBestMatch")
    @field:JsonProperty("isBestMatch")
    val isBestMatch: Boolean
) {

    override fun toString(): String {
        return ToStringBuilder(this, BinanceApiConstants.TO_STRING_BUILDER_STYLE).append("id", id)
            .append("symbol", symbol).append("price", price).append("qty", qty).append("quoteQty", quoteQty)
            .append("commission", commission).append("commissionAsset", commissionAsset).append("time", time)
            .append("buyer", isBuyer).append("maker", isMaker).append("bestMatch", isBestMatch).append("orderId", orderId)
            .toString()
    }
}