package com.binance.api.client.domain.event

import com.binance.api.client.domain.account.AssetBalance
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import tools.jackson.databind.annotation.JsonDeserialize

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

    @get:JsonProperty("B")
    @field:JsonProperty("B")
    @get:JsonDeserialize(contentUsing = AssetBalanceDeserializer::class)
    var balances: List<AssetBalance> = mutableListOf()
)