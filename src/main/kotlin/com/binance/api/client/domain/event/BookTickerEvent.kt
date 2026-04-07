package com.binance.api.client.domain.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class BookTickerEvent(
    @field:JsonProperty("u")
    @get:JsonProperty("u")
    var lastUpdateId: Long = 0,

    @field:JsonProperty("s")
    @get:JsonProperty("s")
    var symbol: String? = null,

    @field:JsonProperty("b")
    @get:JsonProperty("b")
    var bidPrice: String? = null,

    @field:JsonProperty("B")
    @get:JsonProperty("B")
    var bidQty: String? = null,

    @field:JsonProperty("a")
    @get:JsonProperty("a")
    var askPrice: String? = null,

    @field:JsonProperty("A")
    @get:JsonProperty("A")
    var askQty: String? = null
)