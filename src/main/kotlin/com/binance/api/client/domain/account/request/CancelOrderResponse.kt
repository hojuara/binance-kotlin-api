package com.binance.api.client.domain.account.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Response object returned when an order is canceled.
 *
 * @see CancelOrderRequest for the request
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class CancelOrderResponse(
    val symbol: String,

    val origClientOrderId: String,

    val orderId: String,

    val clientOrderId: String,

    val status: String,

    val executedQty: String,

    val origQuoteOrderQty: String,

    val cummulativeQuoteQty: String
)