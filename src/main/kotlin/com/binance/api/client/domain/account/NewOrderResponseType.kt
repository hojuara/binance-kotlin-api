package com.binance.api.client.domain.account

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Desired response type of NewOrder requests.
 *
 * @see NewOrderResponse
 */
@JsonIgnoreProperties(ignoreUnknown = true)
enum class NewOrderResponseType {
    ACK,
    RESULT,
    FULL
}