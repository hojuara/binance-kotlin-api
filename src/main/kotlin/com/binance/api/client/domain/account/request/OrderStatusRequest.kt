package com.binance.api.client.domain.account.request

/**
 * A specialized order request with additional filters.
 */
class OrderStatusRequest : OrderRequest {

    var orderId: Long? = null
    var origClientOrderId: String? = null

    constructor(symbol: String, orderId: Long) : super(symbol) {
        this.orderId = orderId
    }

    constructor(symbol: String, origClientOrderId: String) : super(symbol) {
        this.origClientOrderId = origClientOrderId
    }

    fun orderId(orderId: Long?): OrderStatusRequest {
        this.orderId = orderId
        return this
    }

    fun origClientOrderId(origClientOrderId: String?): OrderStatusRequest {
        this.origClientOrderId = origClientOrderId
        return this
    }

    override fun toString(): String {
        return "OrderStatusRequest(symbol=${symbol}, orderId=$orderId, origClientOrderId=$origClientOrderId)"
    }
}