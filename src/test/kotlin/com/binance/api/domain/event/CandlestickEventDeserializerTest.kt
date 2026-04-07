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
            {
                "e": "kline",
                "E": 1,
                "s": "ETHBTC",
                "k": {
                    "t": 1499404860000,
                    "T": 1499404919999,
                    "s": "ETHBTC",
                    "i": "1m",
                    "f": 77462,
                    "L": 77465,
                    "o": "0.10278577",
                    "c": "0.10278645",
                    "h": "0.10278712",
                    "l": "0.10278518",
                    "v": "17.47929838",
                    "n": 4,
                    "x": false,
                    "q": "1.79662878",
                    "V": "2.34879839",
                    "Q": "0.24142166",
                    "B": "13279784.01349473"
                }
            }
        """.trimIndent()

        val mapper = JsonMapperUtils.getInstance()
        try {
            val candlestickEvent = mapper.readValue(candlestickEventJson, CandlestickEvent::class.java)

            // Verificações do Evento Base
            assertEquals("kline", candlestickEvent.eventType)
            assertEquals(1L, candlestickEvent.eventTime)
            assertEquals("ETHBTC", candlestickEvent.symbol)

            // Verificações do Detalhe do Candlestick (Kline)
            assertEquals(1499404860000L, candlestickEvent.openTime)
            assertEquals("0.10278577", candlestickEvent.open)
            assertEquals("0.10278712", candlestickEvent.high)
            assertEquals("0.10278518", candlestickEvent.low)
            assertEquals("0.10278645", candlestickEvent.close)
            assertEquals("17.47929838", candlestickEvent.volume)
            assertEquals(1499404919999L, candlestickEvent.closeTime)
            assertEquals("1m", candlestickEvent.intervalId)
            assertEquals(77462L, candlestickEvent.firstTradeId)
            assertEquals(77465L, candlestickEvent.lastTradeId)
            assertEquals("1.79662878", candlestickEvent.quoteAssetVolume)
            assertEquals(4L, candlestickEvent.numberOfTrades)
            assertEquals("2.34879839", candlestickEvent.takerBuyBaseAssetVolume)
            assertEquals("0.24142166", candlestickEvent.takerBuyQuoteAssetVolume)
            assertEquals(false, candlestickEvent.isBarFinal)

        } catch (e: IOException) {
            fail("Erro na desserialização: ${e.message}")
        }
    }
}