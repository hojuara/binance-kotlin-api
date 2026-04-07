package com.binance.api.client.domain.event

import com.binance.api.client.domain.account.AssetBalance
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode
import tools.jackson.databind.ValueDeserializer
import java.io.IOException

/**
 * Custom deserializer for an AssetBalance, since the streaming API returns an
 * object in the format {"a":"symbol","f":"free","l":"locked"}, which is
 * different than the format used in the REST API.
 */
class AssetBalanceDeserializer : ValueDeserializer<AssetBalance>() {

    @Throws(IOException::class)
    override fun deserialize(jp: JsonParser?, ctxt: DeserializationContext?): AssetBalance? {
        return jp?.readValueAsTree<JsonNode>()?.let { node ->

            val asset = node["a"].asString()
            val free = node["f"].asString()
            val locked = node["l"].asString()

            AssetBalance().apply {
                this.asset = asset
                this.free = free
                this.locked = locked
            }
        }
    }
}