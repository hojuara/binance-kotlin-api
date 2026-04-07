package com.binance.api.domain.market

import com.binance.api.client.domain.market.Candlestick
import com.binance.api.client.utils.JsonMapperUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import java.io.IOException

/**
 * Tests the adequate deserialization of candlestick JSON information.
 */
class CandlestickDeserializerTest {

    @Test
    fun `should deserialize candlestick from array correctly`() {
        val candlestickJson = """
            [
                1499040000000,
                "0.01634790",
                "0.80000000",
                "0.01575800",
                "0.01577100",
                "148976.11427815",
                1499644799999,
                "2434.19055334",
                308,
                "1756.87402397",
                "28.46694368",
                "17928899.62484339"
            ]
        """.trimIndent()

        val mapper = JsonMapperUtils.getInstance()
        try {
            val candlestick = mapper.readValue(candlestickJson, Candlestick::class.java)

            // Verificações de Tempo
            assertEquals(1499040000000L, candlestick.openTime)
            assertEquals(1499644799999L, candlestick.closeTime)

            // Verificações de Preço (OHLC)
            assertEquals("0.01634790", candlestick.open)
            assertEquals("0.80000000", candlestick.high)
            assertEquals("0.01575800", candlestick.low)
            assertEquals("0.01577100", candlestick.close)

            // Verificações de Volume e Estatísticas
            assertEquals("148976.11427815", candlestick.volume)
            assertEquals("2434.19055334", candlestick.quoteAssetVolume)
            assertEquals(308L, candlestick.numberOfTrades)
            assertEquals("1756.87402397", candlestick.takerBuyBaseAssetVolume)
            assertEquals("28.46694368", candlestick.takerBuyQuoteAssetVolume)

        } catch (e: IOException) {
            fail("Falha na desserialização do Candlestick: ${e.message}")
        }
    }
}