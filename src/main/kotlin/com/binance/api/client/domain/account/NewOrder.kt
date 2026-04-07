package com.binance.api.client.domain.account

import com.binance.api.client.constant.BinanceApiConstants
import com.binance.api.client.domain.OrderSide
import com.binance.api.client.domain.OrderType
import com.binance.api.client.domain.TimeInForce
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * A trade order to enter or exit a position.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class NewOrder(
    /**
     * Symbol to place the order on.
     */
    var symbol: String? = null,

    /**
     * Buy/Sell order side.
     */
    var side: OrderSide? = null,

    /**
     * Type of order.
     */
    var type: OrderType? = null,

    /**
     * Time in force to indicate how long will the order remain active.
     */
    var timeInForce: TimeInForce? = null,

    /**
     * Quantity.
     */
    var quantity: String? = null,

    /**
     * Qutoe quantity.
     */
    var quoteOrderQty: String? = null,

    /**
     * Price.
     */
    var price: String? = null,

    /**
     * A unique id for the order. Automatically generated if not sent.
     */
    var newClientOrderId: String? = null,

    /**
     * Used with stop orders.
     */
    var stopPrice: String? = null,

    /**
     * Used with iceberg orders.
     */
    var icebergQty: String? = null,

    /**
     * Set the response JSON. ACK, RESULT, or FULL; default: RESULT.
     */
    var newOrderRespType: NewOrderResponseType? = NewOrderResponseType.RESULT,

    /**
     * Receiving window.
     */
    var recvWindow: Long? = BinanceApiConstants.DEFAULT_RECEIVING_WINDOW,

    /**
     * Order timestamp.
     */
    var timestamp: Long = System.currentTimeMillis()
) {

    /**
     * Companion object para manter os métodos estáticos de fábrica.
     */
    companion object {
        /**
         * Places a MARKET buy order for the given <code>quantity</code>.
         *
         * @return a new order which is pre-configured with MARKET as the order type and
         * BUY as the order side.
         */
        @JvmStatic
        fun marketBuy(symbol: String, quantity: String?): NewOrder =
            NewOrder(symbol = symbol, side = OrderSide.BUY, type = OrderType.MARKET, quantity = quantity)

        /**
         * Places a MARKET buy order for the given <code>quoteOrderQty</code>.
         *
         * @return a new order which is pre-configured with MARKET as the order type and
         * BUY as the order side. The <code>quoteOrderQty</code> specifies the amount the user wants to spend (when buying).
         */
        @JvmStatic
        fun marketBuyByQuote(symbol: String, quoteOrderQty: String): NewOrder =
            marketBuy(symbol, null).apply { this.quoteOrderQty = quoteOrderQty }

        /**
         * Places a MARKET sell order for the given <code>quantity</code>.
         *
         * @return a new order which is pre-configured with MARKET as the order type and
         * SELL as the order side.
         */
        @JvmStatic
        fun marketSell(symbol: String, quantity: String): NewOrder =
            NewOrder(symbol = symbol, side = OrderSide.SELL, type = OrderType.MARKET, quantity = quantity)

        /**
         * Places a LIMIT buy order for the given <code>quantity</code> and
         * <code>price</code>.
         *
         * @return a new order which is pre-configured with LIMIT as the order type and
         * BUY as the order side.
         */
        @JvmStatic
        fun limitBuy(symbol: String, timeInForce: TimeInForce, quantity: String, price: String): NewOrder =
            NewOrder(symbol = symbol, side = OrderSide.BUY, type = OrderType.LIMIT, timeInForce = timeInForce, quantity = quantity, price = price)

        /**
         * Places a LIMIT sell order for the given <code>quantity</code> and
         * <code>price</code>.
         *
         * @return a new order which is pre-configured with LIMIT as the order type and
         * SELL as the order side.
         */
        @JvmStatic
        fun limitSell(symbol: String, timeInForce: TimeInForce, quantity: String, price: String): NewOrder =
            NewOrder(symbol = symbol, side = OrderSide.SELL, type = OrderType.LIMIT, timeInForce = timeInForce, quantity = quantity, price = price)

        /**
         * Places a STOP LOSS LIMIT buy order for the given <code>quantity</code>,
         * <code>price</code> and <code>stopPrice</code>.
         *
         * @return a new order which is pre-configured with STOP LOSS LIMIT as the order type and
         * BUY as the order side.
         */
        @JvmStatic
        fun stopLossLimitBuy(symbol: String, timeInForce: TimeInForce, quantity: String, price: String, stopPrice: String): NewOrder =
            NewOrder(symbol = symbol, side = OrderSide.BUY, type = OrderType.STOP_LOSS_LIMIT, timeInForce = timeInForce, quantity = quantity, price = price, stopPrice = stopPrice)

        /**
         * Places a STOP LOSS LIMIT buy order for the given <code>quantity</code>,
         * <code>price</code> and <code>stopPrice</code>.
         *
         * @return a new order which is pre-configured with STOP LOSS LIMIT as the order type and
         * SELL as the order side.
         */
        @JvmStatic
        fun stopLossLimitSell(symbol: String, timeInForce: TimeInForce, quantity: String, price: String, stopPrice: String): NewOrder =
            NewOrder(symbol = symbol, side = OrderSide.SELL, type = OrderType.STOP_LOSS_LIMIT, timeInForce = timeInForce, quantity = quantity, price = price, stopPrice = stopPrice)
    }
}