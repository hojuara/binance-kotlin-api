package com.binance.api.client.domain.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import tools.jackson.databind.annotation.JsonDeserialize

/**
 * User data update event which can be of two types:
 *
 * 1) outboundAccountInfo, whenever there is a change in the account (e.g.
 * balance of an asset) 2) outboundAccountPosition, the change in account
 * balances caused by an event. 3) executionReport, whenever there is a trade or
 * an order
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = UserDataUpdateEventDeserializer::class)
data class UserDataUpdateEvent(
    var eventType: UserDataUpdateEventType? = null,

    var eventTime: Long = 0,

    var accountUpdateEvent: AccountUpdateEvent? = null,

    var orderTradeUpdateEvent: OrderTradeUpdateEvent? = null
) {

    override fun toString(): String {
        val base = "UserDataUpdateEvent(eventType=$eventType, eventTime=$eventTime"
        return when (eventType) {
            UserDataUpdateEventType.ACCOUNT_UPDATE -> "$base, accountUpdateEvent=$accountUpdateEvent)"
            UserDataUpdateEventType.ACCOUNT_POSITION_UPDATE -> "$base, accountPositionUpdateEvent=$accountUpdateEvent)"
            else -> "$base, orderTradeUpdateEvent=$orderTradeUpdateEvent)"
        }
    }

    enum class UserDataUpdateEventType(val eventTypeId: String) {
        ACCOUNT_UPDATE("outboundAccountInfo"),
        ACCOUNT_POSITION_UPDATE("outboundAccountPosition"),
        ORDER_TRADE_UPDATE("executionReport");

        companion object {
            @JvmStatic
            fun fromEventTypeId(eventTypeId: String): UserDataUpdateEventType {
                return values().find { it.eventTypeId == eventTypeId }
                    ?: throw IllegalArgumentException("Unrecognized user data update event type id: $eventTypeId")
            }
        }
    }
}