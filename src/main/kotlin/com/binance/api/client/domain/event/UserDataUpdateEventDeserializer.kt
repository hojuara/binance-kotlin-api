package com.binance.api.client.domain.event

import com.binance.api.client.domain.event.UserDataUpdateEvent.UserDataUpdateEventType
import com.binance.api.client.exception.BinanceApiException
import com.binance.api.client.utils.JsonMapperUtils
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.ValueDeserializer
import tools.jackson.module.kotlin.contains
import java.io.IOException

/**
 * Custom deserializer for a User Data stream event, since the API can return
 * two different responses in this stream.
 *
 * @see UserDataUpdateEvent
 */
class UserDataUpdateEventDeserializer : ValueDeserializer<UserDataUpdateEvent>() {

    @Throws(IOException::class)
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): UserDataUpdateEvent? {
        val mapper = JsonMapperUtils.getInstance()

        return p?.let {
            val node = it.readValueAsTree<JsonNode>()
            node["error"]?.run { throw BinanceApiException(node["error"]["msg"].asString()) }
            if (node["event"] == null) return null

            val event = node["event"]
            val eventTypeId = event["e"].asString()
            val eventTime = event["E"].asLong()
            val userDataUpdateEventType = UserDataUpdateEventType.fromEventTypeId(eventTypeId)

            return UserDataUpdateEvent().apply {
                eventType = userDataUpdateEventType
                this.eventTime = eventTime

                val json = event.toString()
                if (userDataUpdateEventType == UserDataUpdateEventType.ACCOUNT_UPDATE) {
                    accountUpdateEvent = getUserDataUpdateEventDetail(json, AccountUpdateEvent::class.java, mapper)
                } else {
                    orderTradeUpdateEvent = getUserDataUpdateEventDetail(json, OrderTradeUpdateEvent::class.java, mapper)
                }
            }
        }

    }

    fun <T> getUserDataUpdateEventDetail(json: String, clazz: Class<T>, mapper: ObjectMapper): T {
        return try {
            mapper.readValue(json, clazz)
        } catch (e: IOException) {
            throw BinanceApiException(e)
        }
    }
}