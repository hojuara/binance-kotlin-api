package com.binance.api.client.domain.account

import com.binance.api.client.constant.BinanceApiConstants
import com.binance.api.client.domain.OrderSide
import com.binance.api.client.domain.OrderStatus
import com.binance.api.client.domain.OrderType
import com.binance.api.client.domain.TimeInForce
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.apache.commons.lang3.builder.ToStringBuilder

/**
 * Trade order information.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Order(
    var symbol: String? = null,
    var orderId: Long? = null,
    var clientOrderId: String? = null,
    var price: String? = null,
    var origQty: String? = null,
    var executedQty: String? = null,
    var status: OrderStatus? = null,
    var timeInForce: TimeInForce? = null,
    var type: OrderType? = null,
    var side: OrderSide? = null,
    var stopPrice: String? = null,
    var icebergQty: String? = null,
    var time: Long = 0L,
    var cummulativeQuoteQty: String? = null
) {
    override fun toString(): String =
        ToStringBuilder(this, BinanceApiConstants.TO_STRING_BUILDER_STYLE)
            .append("symbol", symbol)
            .append("orderId", orderId)
            .append("clientOrderId", clientOrderId)
            .append("price", price)
            .append("origQty", origQty)
            .append("executedQty", executedQty)
            .append("status", status)
            .append("timeInForce", timeInForce)
            .append("type", type)
            .append("side", side)
            .append("stopPrice", stopPrice)
            .append("icebergQty", icebergQty)
            .append("time", time)
            .append("cummulativeQuoteQty", cummulativeQuoteQty)
            .toString()
}

