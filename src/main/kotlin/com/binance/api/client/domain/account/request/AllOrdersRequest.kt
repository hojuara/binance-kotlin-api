package com.binance.api.client.domain.account.request

import com.binance.api.client.constant.BinanceApiConstants

/**
 * A specialized order request with additional filters.
 */
class AllOrdersRequest(symbol: String) : OrderRequest(symbol) {

    var orderId: Long? = null
        private set

    var limit: Int? = DEFAULT_LIMIT
        private set

    fun orderId(orderId: Long?): AllOrdersRequest {
        this.orderId = orderId
        return this
    }

    fun limit(limit: Int?): AllOrdersRequest {
        this.limit = limit
        return this
    }

    override fun toString(): String {
        return "AllOrdersRequest(orderId=$orderId, limit=$limit, symbol=${symbol})"
    }

    companion object {
        private const val DEFAULT_LIMIT = 500
    }
}