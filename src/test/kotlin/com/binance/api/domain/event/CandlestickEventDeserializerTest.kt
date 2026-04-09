package com.binance.api.domain.event

import com.binance.api.client.domain.event.CandlestickEvent
import com.binance.api.client.utils.JsonMapperUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import java.io.IOException

/**
 * Tests that JSON responses from a candlestick event are converted to the
 * appropriate [CandlestickEvent] object.
 */
class CandlestickEventDeserializerTest {

    @Test
    fun `test candlestick event deserializer`() {
        val candlestickEventJson = """
            [
                1655971200000,
                "0.01086000",
                "0.01086600",
                "0.01083600",
                "0.01083800",
                "2290.53800000",
                1655974799999,
                "24.85074442",
                2283,
                "1171.64000000",
                "12.71225884"
            ]
        """.trimIndent()

        val mapper = JsonMapperUtils.getInstance()
        try {
            val candlestickEvent = mapper.readValue(candlestickEventJson, CandlestickEvent::class.java)

            // Verificações do Detalhe do Candlestick (Kline)
            assertEquals(1655971200000L, candlestickEvent.openTime)
            assertEquals("0.01086000", candlestickEvent.open)
            assertEquals("0.01086600", candlestickEvent.high)
            assertEquals("0.01083600", candlestickEvent.low)
            assertEquals("0.01083800", candlestickEvent.close)
            assertEquals("2290.53800000", candlestickEvent.volume)
            assertEquals(1655974799999L, candlestickEvent.closeTime)
            assertEquals("24.85074442", candlestickEvent.quoteAssetVolume)
            assertEquals(2283L, candlestickEvent.numberOfTrades)
            assertEquals("1171.64000000", candlestickEvent.takerBuyBaseAssetVolume)
            assertEquals("12.71225884", candlestickEvent.takerBuyQuoteAssetVolume)

        } catch (e: IOException) {
            fail("Erro na desserialização: ${e.message}")
        }
    }
}