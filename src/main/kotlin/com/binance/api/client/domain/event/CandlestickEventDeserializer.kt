package com.binance.api.client.domain.event

import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode
import tools.jackson.databind.ValueDeserializer
import java.io.IOException

/**
 * Custom deserializer for a candlestick stream event, since the structure of
 * the candlestick json differ from the one in the REST API.
 *
 * @see CandlestickEvent
 */
class CandlestickEventDeserializer : ValueDeserializer<CandlestickEvent>() {

    @Throws(IOException::class)
    override fun deserialize(jp: JsonParser?, ctxt: DeserializationContext?): CandlestickEvent? {
        return jp?.readValueAsTree<JsonNode>()?.let { node ->
            val candlestickNode = node["k"]
            CandlestickEvent(
                // Parse header
                eventType = node["e"].asString(),
                eventTime = node["E"].asLong(),
                symbol = node["s"].asString(),

                // Parse candlestick data
                openTime = candlestickNode["t"].asLong(),
                closeTime = candlestickNode["T"].asLong(),
                intervalId = candlestickNode["i"].asString(),
                firstTradeId = candlestickNode["f"].asLong(),
                lastTradeId = candlestickNode["L"].asLong(),
                open = candlestickNode["o"].asString(),
                close = candlestickNode["c"].asString(),
                high = candlestickNode["h"].asString(),
                low = candlestickNode["l"].asString(),
                volume = candlestickNode["v"].asString(),
                numberOfTrades = candlestickNode["n"].asLong(),
                isBarFinal = candlestickNode["x"].asBoolean(),
                quoteAssetVolume = candlestickNode["q"].asString(),
                takerBuyBaseAssetVolume = candlestickNode["V"].asString(),
                takerBuyQuoteAssetVolume = candlestickNode["Q"].asString()
            )
        }
    }
}