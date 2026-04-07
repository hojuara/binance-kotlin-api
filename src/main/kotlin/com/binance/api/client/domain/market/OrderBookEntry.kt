package com.binance.api.client.domain.market

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize

/**
 * An order book entry consisting of price and quantity.
 */
@JsonDeserialize(using = OrderBookEntryDeserializer::class)
@JsonSerialize(using = OrderBookEntrySerializer::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class OrderBookEntry(
    val price: String,
    val qty: String
)