package com.binance.api.client.domain.event

import com.binance.api.client.domain.*
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Order or trade report update event.
 *
 * This event is embedded as part of a user data update event.
 *
 * @see UserDataUpdateEvent
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class OrderTradeUpdateEvent(
    @field:JsonProperty("e")
    @get:JsonProperty("e")
    var eventType: String? = null,

    @field:JsonProperty("E")
    @get:JsonProperty("E")
    var eventTime: Long? = null,

    @field:JsonProperty("s")
    @get:JsonProperty("s")
    var symbol: String? = null,

    @field:JsonProperty("c")
    @get:JsonProperty("c")
    var newClientOrderId: String? = null,

    /**
     * Buy/Sell order side.
     */
    @field:JsonProperty("S")
    @get:JsonProperty("S")
    var side: OrderSide? = null,

    /**
     * Type of order.
     */
    @field:JsonProperty("o")
    @get:JsonProperty("o")
    var type: OrderType? = null,

    /**
     * Time in force to indicate how long will the order remain active.
     */
    @field:JsonProperty("f")
    @get:JsonProperty("f")
    var timeInForce: TimeInForce? = null,

    /**
     * Original quantity in the order.
     */
    @field:JsonProperty("q")
    @get:JsonProperty("q")
    var originalQuantity: String? = null,

    /**
     * Price.
     */
    @field:JsonProperty("p")
    @get:JsonProperty("p")
    var price: String? = null,

    /**
     * Type of execution.
     */
    @field:JsonProperty("x")
    @get:JsonProperty("x")
    var executionType: ExecutionType? = null,

    /**
     * Status of the order.
     */
    @field:JsonProperty("X")
    @get:JsonProperty("X")
    var orderStatus: OrderStatus? = null,

    /**
     * Reason why the order was rejected.
     */
    @field:JsonProperty("r")
    @get:JsonProperty("r")
    var orderRejectReason: OrderRejectReason? = null,

    /**
     * Order id.
     */
    @field:JsonProperty("i")
    @get:JsonProperty("i")
    var orderId: Long? = null,

    /**
     * Quantity of the last filled trade.
     */
    @field:JsonProperty("l")
    @get:JsonProperty("l")
    var quantityLastFilledTrade: String? = null,

    /**
     * Accumulated quantity of filled trades on this order.
     */
    @field:JsonProperty("z")
    @get:JsonProperty("z")
    var accumulatedQuantity: String? = null,

    /**
     * Price of last filled trade.
     */
    @field:JsonProperty("L")
    @get:JsonProperty("L")
    var priceOfLastFilledTrade: String? = null,

    /**
     * Commission.
     */
    @field:JsonProperty("n")
    @get:JsonProperty("n")
    var commission: String? = null,

    /**
     * Asset on which commission is taken
     */
    @field:JsonProperty("N")
    @get:JsonProperty("N")
    var commissionAsset: String? = null,

    /**
     * Order/trade time.
     */
    @field:JsonProperty("T")
    @get:JsonProperty("T")
    var orderTradeTime: Long? = null,

    /**
     * Trade id.
     */
    @field:JsonProperty("t")
    @get:JsonProperty("t")
    var tradeId: Long? = null
)