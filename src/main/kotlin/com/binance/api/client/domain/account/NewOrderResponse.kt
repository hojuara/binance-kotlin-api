package com.binance.api.client.domain.account

import com.binance.api.client.constant.BinanceApiConstants
import com.binance.api.client.domain.OrderSide
import com.binance.api.client.domain.OrderStatus
import com.binance.api.client.domain.OrderType
import com.binance.api.client.domain.TimeInForce
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.apache.commons.lang3.builder.ToStringBuilder

/**
 * Response returned when placing a new order on the system.
 *
 * @see NewOrder for the request
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class NewOrderResponse(
    val symbol: String,
    val orderId: Long,
    val orderListId: Long,
    val clientOrderId: String,
    val transactTime: Long,
    val price: String?,
    val origQty: String?,
    val executedQty: String?,
    val cummulativeQuoteQty: String?,
    val status: OrderStatus?,
    val timeInForce: TimeInForce?,
    val type: OrderType?,
    val side: OrderSide?,
    val fills: List<Trade>?
) {
    override fun toString(): String {
        val fillsString = (fills ?: emptyList())
            .joinToString(", ") { it.toString() }

        return ToStringBuilder(this, BinanceApiConstants.TO_STRING_BUILDER_STYLE)
            .append("symbol", symbol)
            .append("orderId", orderId)
            .append("clientOrderId", clientOrderId)
            .append("transactTime", transactTime)
            .append("price", price)
            .append("origQty", origQty)
            .append("executedQty", executedQty)
            .append("status", status)
            .append("timeInForce", timeInForce)
            .append("type", type)
            .append("side", side)
            .append("fills", "[$fillsString]")
            .toString()
    }
}

