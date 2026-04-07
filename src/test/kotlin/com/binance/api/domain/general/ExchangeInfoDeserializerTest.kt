package com.binance.api.domain.general

import com.binance.api.client.domain.OrderType
import com.binance.api.client.domain.general.*
import com.binance.api.client.utils.JsonMapperUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.IOException

/**
 * Test deserialization of exchange information.
 */
class ExchangeInfoDeserializerTest {

    @Test
    fun `should deserialize exchange information correctly`() {
        val json = """
            {
              "timezone": "UTC",
              "serverTime": 1508631584636,
              "rateLimits": [
                {
                  "rateLimitType": "REQUEST_WEIGHT",
                  "interval": "MINUTE",
                  "limit": 1200
                },
                {
                  "rateLimitType": "ORDERS",
                  "interval": "SECOND",
                  "limit": 10
                },
                {
                  "rateLimitType": "ORDERS",
                  "interval": "DAY",
                  "limit": 100000
                }
              ],
              "exchangeFilters": [],
              "symbols": [{
                "symbol": "ETHBTC",
                "status": "TRADING",
                "baseAsset": "ETH",
                "baseAssetPrecision": 8,
                "quoteAsset": "BTC",
                "quotePrecision": 8,
                "orderTypes": ["LIMIT", "MARKET"],
                "icebergAllowed": false,
                "filters": [
                  {
                    "filterType": "PRICE_FILTER",
                    "minPrice": "0.00000100",
                    "maxPrice": "100000.00000000",
                    "tickSize": "0.00000100"
                  }, 
                  {
                    "filterType": "LOT_SIZE",
                    "minQty": "0.00100000",
                    "maxQty": "100000.00000000",
                    "stepSize": "0.00100000"
                  }, 
                  {
                    "filterType": "NOTIONAL",
                    "minNotional": "0.00010000",
                    "maxNotional": "9000000.00000000"
                  }
                ],
                "permissions": ["SPOT", "MARGIN"]
              }]
            }
        """.trimIndent()

        val mapper = JsonMapperUtils.getInstance()
        try {
            val exchangeInfo = mapper.readValue(json, ExchangeInfo::class.java)

            assertEquals("UTC", exchangeInfo.timezone)
            assertEquals(1508631584636L, exchangeInfo.serverTime)

            // Teste de Rate Limits
            val rateLimits = exchangeInfo.rateLimits ?: fail("Rate limits should not be null")
            assertEquals(3, rateLimits.size)
            testRateLimit(rateLimits[0], RateLimitType.REQUEST_WEIGHT, RateLimitInterval.MINUTE, 1200)
            testRateLimit(rateLimits[1], RateLimitType.ORDERS, RateLimitInterval.SECOND, 10)
            testRateLimit(rateLimits[2], RateLimitType.ORDERS, RateLimitInterval.DAY, 100000)

            // Teste de Symbols
            val symbols = exchangeInfo.symbols ?: fail("Symbols should not be null")
            assertEquals(1, symbols.size)

            val symbolInfo = symbols[0]
            assertEquals("ETHBTC", symbolInfo.symbol)
            assertEquals(SymbolStatus.TRADING, symbolInfo.status)
            assertEquals("ETH", symbolInfo.baseAsset)
            assertEquals(8, symbolInfo.baseAssetPrecision)
            assertEquals("BTC", symbolInfo.quoteAsset)
            assertEquals(8, symbolInfo.quotePrecision)
            assertEquals(listOf(OrderType.LIMIT, OrderType.MARKET), symbolInfo.orderTypes)
            assertFalse(symbolInfo.isIcebergAllowed)

            // Teste de Filters
            val symbolFilters = symbolInfo.filters ?: fail("Filters should not be null")
            assertEquals(3, symbolFilters.size)

            val priceFilter = symbolFilters[0]
            assertEquals(FilterType.PRICE_FILTER, priceFilter.filterType)
            assertEquals("0.00000100", priceFilter.minPrice)
            assertEquals("100000.00000000", priceFilter.maxPrice)
            assertEquals("0.00000100", priceFilter.tickSize)

            val lotSizeFilter = symbolFilters[1]
            assertEquals(FilterType.LOT_SIZE, lotSizeFilter.filterType)
            assertEquals("0.00100000", lotSizeFilter.minQty)
            assertEquals("100000.00000000", lotSizeFilter.maxQty)
            assertEquals("0.00100000", lotSizeFilter.stepSize)

            val notionalFilter = symbolFilters[2]
            assertEquals(FilterType.NOTIONAL, notionalFilter.filterType)
            assertEquals("0.00010000", notionalFilter.minNotional)
            assertEquals("9000000.00000000", notionalFilter.maxNotional)

            assertEquals(2, symbolInfo.permissions?.size)
            assertEquals("SPOT", symbolInfo.permissions?.get(0))

        } catch (e: IOException) {
            fail("Deserialization failed: ${e.message}")
        }
    }

    private fun testRateLimit(
        rateLimit: RateLimit,
        expectedType: RateLimitType,
        expectedInterval: RateLimitInterval,
        expectedLimit: Int
    ) {
        assertEquals(expectedType, rateLimit.rateLimitType)
        assertEquals(expectedInterval, rateLimit.interval)
        assertEquals(expectedLimit, rateLimit.limit)
    }
}