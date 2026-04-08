package com.binance.api.client.domain.account.request

import com.binance.api.client.constant.BinanceApiConstants
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Base request parameters for order-related methods.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
open class OrderRequest(symbol: String?) {

    var symbol: String? = symbol
        private set

    var recvWindow: Long? = BinanceApiConstants.DEFAULT_RECEIVING_WINDOW
        private set

    var timestamp: Long = System.currentTimeMillis()
        private set

    fun symbol(symbol: String) = apply { this.symbol = symbol }

    fun recvWindow(recvWindow: Long) = apply { this.recvWindow = recvWindow }

    fun timestamp(timestamp: Long) = apply { this.timestamp = timestamp }

    override fun toString(): String {
        return "OrderRequest(symbol='$symbol', recvWindow=$recvWindow, timestamp=$timestamp)"
    }
}