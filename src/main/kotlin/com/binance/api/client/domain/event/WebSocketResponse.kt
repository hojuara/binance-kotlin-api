package com.binance.api.client.domain.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import tools.jackson.databind.JsonNode

@JsonIgnoreProperties(ignoreUnknown = true)
data class WebSocketResponse(
    val id: String?,
    val status: Int?,

    @field:JsonProperty("event")
    @get:JsonProperty("event")
    val event: JsonNode?,

    @field:JsonProperty("result")
    @get:JsonProperty("result")
    val result: JsonNode?
)