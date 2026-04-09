package com.binance.api.client.domain.event

import com.fasterxml.jackson.annotation.JsonProperty

data class AssetBalanceEvent(
    @get:JsonProperty("a")
    @field:JsonProperty("a")
    var asset: String,

    @get:JsonProperty("f")
    @field:JsonProperty("f")
    var free: String,

    @get:JsonProperty("l")
    @field:JsonProperty("l")
    var locked: String
)
