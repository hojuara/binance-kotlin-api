package com.binance.api.client.domain.general

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class CirculationSupplyInfo(
    @field:JsonProperty("s")
    @get:JsonProperty("s")
    val symbol: String,

    @field:JsonProperty("cs")
    @get:JsonProperty("cs")
    val circulationSupply: Long
)