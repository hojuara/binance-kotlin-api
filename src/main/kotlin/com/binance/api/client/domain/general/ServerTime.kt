package com.binance.api.client.domain.general

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Time of the server running Binance's REST API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class ServerTime(
    var serverTime: Long
) {
    override fun toString(): String {
        return serverTime.toString()
    }
}