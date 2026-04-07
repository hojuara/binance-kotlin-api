package com.binance.api.client

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Binance API error object.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class BinanceApiError(
    /**
     * Error code.
     */
    var code: Int = 0,

    /**
     * Error message.
     */
    var msg: String? = null
)