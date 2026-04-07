package com.binance.api.client.domain.event

import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueSerializer
import java.io.IOException

/**
 * Custom serializer for a candlestick stream event, since the structure of the
 * candlestick json differ from the one in the REST API.
 *
 * @see CandlestickEvent
 */
class CandlestickEventSerializer : ValueSerializer<CandlestickEvent>() {

    @Throws(IOException::class)
    override fun serialize(value: CandlestickEvent?, gen: JsonGenerator?, ctxt: SerializationContext?) {
        value?.also { candlestickEvent ->
            gen?.run {
                writeStartObject()

                // Write header
                writeStringProperty("e", candlestickEvent.eventType)
                writeNumberProperty("E", candlestickEvent.eventTime)
                writeStringProperty("s", candlestickEvent.symbol)

                // Write candlestick data
                writeObjectPropertyStart("k")
                candlestickEvent.openTime?.let { writeNumberProperty("t", it) }
                candlestickEvent.closeTime?.let { writeNumberProperty("T", it) }
                writeStringProperty("i", candlestickEvent.intervalId)
                candlestickEvent.firstTradeId?.let { writeNumberProperty("f", it) }
                candlestickEvent.lastTradeId?.let { writeNumberProperty("L", it) }
                writeStringProperty("o", candlestickEvent.open)
                writeStringProperty("c", candlestickEvent.close)
                writeStringProperty("h", candlestickEvent.high)
                writeStringProperty("l", candlestickEvent.low)
                writeStringProperty("v", candlestickEvent.volume)
                candlestickEvent.numberOfTrades?.let { writeNumberProperty("n", it) }
                candlestickEvent.isBarFinal?.let { writeBooleanProperty("x", it) }
                writeStringProperty("q", candlestickEvent.quoteAssetVolume)
                writeStringProperty("V", candlestickEvent.takerBuyBaseAssetVolume)
                writeStringProperty("Q", candlestickEvent.takerBuyQuoteAssetVolume)
                writeEndObject()

                writeEndObject()
            }
        }
    }
}