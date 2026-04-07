package com.binance.api.client.exception

import com.binance.api.client.BinanceApiError

/**
 * An exception which can occur while invoking methods of the Binance API.
 */
class BinanceApiException : RuntimeException {

    /**
     * Error response object returned by Binance API.
     * @return the response error object from Binance API, or null if no response
     * object was returned (e.g. server returned 500).
     */
    var error: BinanceApiError? = null
        private set

    constructor() : super()

    constructor(message: String?) : super(message)

    constructor(cause: Throwable?) : super(cause)

    constructor(message: String?, cause: Throwable?) : super(message, cause)

    /**
     * Instantiates a new binance api exception.
     *
     * @param error an error response object
     */
    constructor(error: BinanceApiError) : super() {
        this.error = error
    }

    override val message: String?
        get() = error?.msg ?: super.message

    companion object {
        private const val serialVersionUID = 3788669840036201041L
    }
}