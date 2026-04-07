package com.binance.api.client.domain.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Dummy type to wrap a listen key from a server response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class ListenKey(
    var listenKey: String? = null
) {
    override fun toString(): String {
        return listenKey ?: ""
    }
}