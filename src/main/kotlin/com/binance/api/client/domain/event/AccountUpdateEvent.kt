package com.binance.api.client.domain.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Account update event which will reflect the current position/balances of the
 * account.
 *
 * This event is embedded as part of a user data update event.
 *
 * @see UserDataUpdateEvent
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class AccountUpdateEvent(
    @get:JsonProperty("e")
    @field:JsonProperty("e")
    var eventType: String? = null,

    @get:JsonProperty("E")
    @field:JsonProperty("E")
    var eventTime: Long = 0,

    @get:JsonProperty("u")
    @field:JsonProperty("u")
    var lastAccountUpdate: Long,

    @get:JsonProperty("B")
    @field:JsonProperty("B")
    var balances: List<AssetBalanceEvent> = mutableListOf()
)