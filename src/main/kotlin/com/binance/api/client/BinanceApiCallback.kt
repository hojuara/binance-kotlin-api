package com.binance.api.client

/**
 * BinanceApiCallback is a functional interface used together with the BinanceApiAsyncClient to provide a non-blocking REST client.
 *
 * @param <T> the return type from the callback
</T> */
fun interface BinanceApiCallback<T> {
    /**
     * Called whenever a response comes back from the Binance API.
     *
     * @param response the expected response object
     */
    fun onResponse(response: T)

    /**
     * Called whenever an error occurs.
     *
     * @param cause the cause of the failure
     */
    fun onFailure(cause: Throwable) {}
}