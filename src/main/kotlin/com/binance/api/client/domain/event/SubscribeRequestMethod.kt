package com.binance.api.client.domain.event

import com.fasterxml.jackson.annotation.JsonProperty

enum class SubscribeRequestMethod {

    @JsonProperty("depth")
    DEPTH,

    @JsonProperty("klines")
    CANDLESTICK,

    @JsonProperty("trades.aggregate")
    AGGREGATE_TRADES,

    @JsonProperty("userDataStream.subscribe.signature")
    USER_DATA,

    @JsonProperty("ticker.book")
    ORDER_BOOK_TICKER
}