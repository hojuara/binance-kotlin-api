package com.binance.api.client.domain.account.request

import com.binance.api.client.constant.BinanceApiConstants
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Base request parameters for order-related methods.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
open class OrderRequest(val symbol: String) {

    var recvWindow: Long? = BinanceApiConstants.DEFAULT_RECEIVING_WINDOW
        private set

    var timestamp: Long = System.currentTimeMillis()
        private set

    fun recvWindow(recvWindow: Long?): OrderRequest {
        this.recvWindow = recvWindow
        return this
    }

    fun timestamp(timestamp: Long): OrderRequest {
        this.timestamp = timestamp
        return this
    }

    override fun toString(): String {
        return "OrderRequest(symbol='$symbol', recvWindow=$recvWindow, timestamp=$timestamp)"
    }
}