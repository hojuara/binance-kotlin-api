package com.binance.api.client.domain.market

import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueSerializer
import java.io.IOException

/**
 * Custom serializer for an OrderBookEntry.
 */
class OrderBookEntrySerializer : ValueSerializer<OrderBookEntry>() {

    @Throws(IOException::class)
    override fun serialize(orderBookEntry: OrderBookEntry?, gen: JsonGenerator?, ctxt: SerializationContext?) {
        gen?.run {
            writeStartArray()
            writeString(orderBookEntry?.price)
            writeString(orderBookEntry?.qty)
            writeEndArray()
        }
    }
}