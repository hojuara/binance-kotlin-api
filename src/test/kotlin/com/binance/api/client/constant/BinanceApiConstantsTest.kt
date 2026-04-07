package com.binance.api.client.constant

import com.binance.api.client.domain.market.Candlestick
import com.binance.api.client.utils.JsonMapperUtils
import org.apache.commons.lang3.builder.ToStringStyle
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.io.IOException

/**
 * Test for [BinanceApiConstants]
 */
class BinanceApiConstantsTest {

    companion object {
        private lateinit var candlestick: Candlestick
        private lateinit var defaultToStringBuilderStyle: ToStringStyle

        @JvmStatic
        @BeforeAll
        fun setUpClass() {
            // Salva o estilo original para restaurar depois do teste
            defaultToStringBuilderStyle = BinanceApiConstants.TO_STRING_BUILDER_STYLE

            val candlestickRaw = """
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
                candlestick = mapper.readValue(candlestickRaw, Candlestick::class.java)
            } catch (e: IOException) {
                fail("Falha ao desserializar Candlestick para o teste: ${e.message}")
            }
        }

        @JvmStatic
        @AfterAll
        fun tearDownClass() {
            // Restaura o estado global das constantes
            BinanceApiConstants.TO_STRING_BUILDER_STYLE = defaultToStringBuilderStyle
        }
    }

    @Test
    fun testToStringBuilderStyleChange() {
        // Nota: O teste assume que Candlestick ainda usa o ToStringBuilder em seu toString()
        // ou que estamos validando a alternância da constante global.

        // Default Style
        val binanceApiDefaultStyle = "Candlestick[openTime=1499040000000,open=0.01634790,high=0.80000000,low=0.01575800,close=0.01577100,volume=148976.11427815,closeTime=1499644799999,quoteAssetVolume=2434.19055334,numberOfTrades=308,takerBuyBaseAssetVolume=1756.87402397,takerBuyQuoteAssetVolume=28.46694368]"
        assertEquals(binanceApiDefaultStyle, candlestick.toString())

        // JSON Style
        BinanceApiConstants.TO_STRING_BUILDER_STYLE = ToStringStyle.JSON_STYLE
        val jsonStyle = "{\"openTime\":1499040000000,\"open\":\"0.01634790\",\"high\":\"0.80000000\",\"low\":\"0.01575800\",\"close\":\"0.01577100\",\"volume\":\"148976.11427815\",\"closeTime\":1499644799999,\"quoteAssetVolume\":\"2434.19055334\",\"numberOfTrades\":308,\"takerBuyBaseAssetVolume\":\"1756.87402397\",\"takerBuyQuoteAssetVolume\":\"28.46694368\"}"
        assertEquals(jsonStyle, candlestick.toString())

        // No Class Name Style
        BinanceApiConstants.TO_STRING_BUILDER_STYLE = ToStringStyle.NO_CLASS_NAME_STYLE
        val noClassNameStyle = "[openTime=1499040000000,open=0.01634790,high=0.80000000,low=0.01575800,close=0.01577100,volume=148976.11427815,closeTime=1499644799999,quoteAssetVolume=2434.19055334,numberOfTrades=308,takerBuyBaseAssetVolume=1756.87402397,takerBuyQuoteAssetVolume=28.46694368]"
        assertEquals(noClassNameStyle, candlestick.toString())

        // Short Prefix Style
        BinanceApiConstants.TO_STRING_BUILDER_STYLE = ToStringStyle.SHORT_PREFIX_STYLE
        val shortPrefixStyle = "Candlestick[openTime=1499040000000,open=0.01634790,high=0.80000000,low=0.01575800,close=0.01577100,volume=148976.11427815,closeTime=1499644799999,quoteAssetVolume=2434.19055334,numberOfTrades=308,takerBuyBaseAssetVolume=1756.87402397,takerBuyQuoteAssetVolume=28.46694368]"
        assertEquals(shortPrefixStyle, candlestick.toString())

        // Simple Style
        BinanceApiConstants.TO_STRING_BUILDER_STYLE = ToStringStyle.SIMPLE_STYLE
        val simpleStyle = "1499040000000,0.01634790,0.80000000,0.01575800,0.01577100,148976.11427815,1499644799999,2434.19055334,308,1756.87402397,28.46694368"
        assertEquals(simpleStyle, candlestick.toString())
    }
}