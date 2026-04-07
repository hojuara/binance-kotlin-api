package com.binance.api.client.domain.market

import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode
import tools.jackson.databind.ValueDeserializer
import java.io.IOException

/**
 * Custom deserializer for an OrderBookEntry, since the API returns an array in
 * the format [ price, qty, [] ].
 */
class OrderBookEntryDeserializer : ValueDeserializer<OrderBookEntry?>() {

    @Throws(IOException::class)
    override fun deserialize(jp: JsonParser?, ctxt: DeserializationContext?): OrderBookEntry? {
        return jp?.let {
            val node: JsonNode = it.readValueAsTree<JsonNode>()
            val price = node[0].asString()
            val qty = node[1].asString()
            OrderBookEntry(price, qty)
        }
    }
}