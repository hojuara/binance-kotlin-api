package com.binance.api.client.integration

import com.binance.api.client.BinanceApiRestClient
import com.binance.api.client.domain.market.CandlestickInterval
import com.binance.api.client.domain.account.NewOrder
import com.binance.api.client.domain.account.request.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BinanceApiRestClientIntegrationTest {

    private companion object {
        const val SYMBOL = "BTCUSDT"
    }

    private lateinit var client: BinanceApiRestClient

    @BeforeAll
    fun setUp() {
        BinanceIntegrationTestSupport.requireApiKeysOrSkip()
        client = BinanceIntegrationTestSupport.newRestClient()
    }

    // General endpoints

    @Test
    fun `ping should succeed`() = assertDoesNotThrow { client.ping() }

    @Test
    fun `server time should be returned`() {
        val serverTime = client.serverTime
        assertTrue(serverTime > 0)
    }

    @Test
    fun `exchangeInfo should contain expected top-level fields`() {
        val exchangeInfo = client.exchangeInfo
        assertNotNull(exchangeInfo.timezone)
        assertTrue(exchangeInfo.serverTime > 0)
        assertNotNull(exchangeInfo.symbols)
        assertTrue(exchangeInfo.symbols.isNotEmpty())
        val symbolInfo = exchangeInfo.getSymbolInfo(SYMBOL)
        assertEquals(SYMBOL, symbolInfo.symbol)
    }

    @Test
    fun `allAssets should return list`() {
        val assets = client.allAssets
        assertNotNull(assets)
        assertTrue(assets.isNotEmpty())
    }

    // Market Data endpoints

    @Test
    fun `getOrderBook should return bids and asks`() {
        val orderBook = client.getOrderBook(SYMBOL, 5)
        assertTrue(orderBook.lastUpdateId > 0)
        assertNotNull(orderBook.bids)
        assertNotNull(orderBook.asks)
        assertTrue(orderBook.bids.isNotEmpty())
        assertTrue(orderBook.asks.isNotEmpty())
    }

    @Test
    fun `getTrades should return at least one trade`() {
        val trades = client.getTrades(SYMBOL, 5)
        assertNotNull(trades)
        assertTrue(trades.isNotEmpty())
    }

    // @Test
    fun `getHistoricalTrades should return list (disabled by default)`() {
        // NOTE: Provide a known-good fromId when enabling this test.
        val historical = client.getHistoricalTrades(SYMBOL, 5, 0L)
        assertNotNull(historical)
    }

    @Test
    fun `getAggTrades with params should return list`() {
        val trades = client.getAggTrades(symbol = SYMBOL, fromId = null, limit = 5, startTime = null, endTime = null)
        assertNotNull(trades)
        assertTrue(trades.isNotEmpty())
    }

    @Test
    fun `getAggTrades overload should return list`() {
        val trades = client.getAggTrades(SYMBOL)
        assertNotNull(trades)
        assertTrue(trades.isNotEmpty())
    }

    @Test
    fun `getCandlestickBars with params should return list`() {
        val bars = client.getCandlestickBars(SYMBOL, CandlestickInterval.ONE_MINUTE, 5, null, null)
        assertNotNull(bars)
        assertTrue(bars.isNotEmpty())
    }

    @Test
    fun `getAvgPrice should return value`() {
        val avg = client.getAvgPrice(SYMBOL)
        assertNotNull(avg)
        assertTrue(avg.mins > 0)
        assertTrue(avg.closeTime > 0)
        assertTrue(avg.price.isNotBlank())
    }

    @Test
    fun `getCandlestickBars overload should return list`() {
        val bars = client.getCandlestickBars(SYMBOL, CandlestickInterval.ONE_MINUTE)
        assertNotNull(bars)
        assertTrue(bars.isNotEmpty())
    }

    @Test
    fun `get24HrPriceStatistics should return stats`() {
        val stats = client.get24HrPriceStatistics(SYMBOL)
        assertNotNull(stats)
        assertEquals(SYMBOL, stats.symbol)
    }

    @Test
    fun `all24HrPriceStatistics should return list`() {
        val all = client.all24HrPriceStatistics
        assertNotNull(all)
        assertTrue(all.isNotEmpty())
    }

    @Test
    fun `allPrices should return list`() {
        val all = client.allPrices
        assertNotNull(all)
        assertTrue(all.isNotEmpty())
    }

    @Test
    fun `getPrice should return symbol and price`() {
        val price = client.getPrice(SYMBOL)
        assertEquals(SYMBOL, price.symbol)
        assertFalse(price.price.isNullOrBlank())
    }

    @Test
    fun `bookTickers should return list`() {
        val tickers = client.bookTickers
        assertNotNull(tickers)
        assertTrue(tickers.isNotEmpty())
    }

    // Account endpoints

    // @Test
    fun `newOrder should place real order (disabled by default)`() {
        val order = NewOrder.marketBuy(SYMBOL, "0.00001")
        val response = client.newOrder(order)
        assertNotNull(response)
    }

    // @Test
    fun `newOrderTest should validate order (disabled by default)`() {
        val order = NewOrder.marketBuy(SYMBOL, "0.00001")
        assertDoesNotThrow { client.newOrderTest(order) }
    }

    // @Test
    fun `getOrderStatus should return order (disabled by default)`() {
        val req = OrderStatusRequest(SYMBOL, 0L)
        val order = client.getOrderStatus(req)
        assertNotNull(order)
    }

    // @Test
    fun `cancelOrder should cancel order (disabled by default)`() {
        val req = CancelOrderRequest(SYMBOL, 0L)
        val resp = client.cancelOrder(req)
        assertNotNull(resp)
    }

    // @Test
    fun `getOpenOrders should return list (disabled by default)`() {
        val orders = client.getOpenOrders(OrderRequest(SYMBOL))
        assertNotNull(orders)
    }

    // @Test
    fun `getAllOrders should return list (disabled by default)`() {
        val orders = client.getAllOrders(AllOrdersRequest(SYMBOL).limit(10))
        assertNotNull(orders)
    }

    @Test
    fun `getAccount should be returned for signed endpoint`() {
        val account = client.getAccount(60_000L, System.currentTimeMillis())
        assertNotNull(account)
        assertNotNull(account.balances)
    }

    @Test
    fun `account should be returned for signed endpoint`() {
        val account = client.account
        assertNotNull(account)
        assertNotNull(account.balances)
        assertTrue(account.balances.isNotEmpty())
    }

    // @Test
    fun `getMyTrades with params should return list (disabled by default)`() {
        val trades = client.getMyTrades(SYMBOL, 10, null, 60_000L, System.currentTimeMillis())
        assertNotNull(trades)
    }

    // @Test
    fun `getMyTrades symbol+limit overload should return list (disabled by default)`() {
        val trades = client.getMyTrades(SYMBOL, 10)
        assertNotNull(trades)
    }

    // @Test
    fun `getMyTrades symbol overload should return list (disabled by default)`() {
        val trades = client.getMyTrades(SYMBOL)
        assertNotNull(trades)
    }

    // @Test
    fun `withdraw should create withdraw request (disabled by default)`() {
        val result = client.withdraw("BTC", "address", "0.001", null, null)
        assertNotNull(result)
    }

    // @Test
    fun `getDepositHistory should return array (disabled by default)`() {
        val deposits = client.getDepositHistory("BTC")
        assertNotNull(deposits)
    }

    // @Test
    fun `getWithdrawHistory should return array (disabled by default)`() {
        val withdraws = client.getWithdrawHistory("BTC")
        assertNotNull(withdraws)
    }

    // @Test
    fun `getDepositAddress should return address (disabled by default)`() {
        val address = client.getDepositAddress("BTC")
        assertNotNull(address)
    }

    // @Test
    fun `convertDust should return conversion info (disabled by default)`() {
        val info = client.convertDust(listOf("ETH"))
        assertNotNull(info)
    }

    @Test
    fun `getMarketCap should return value or null`() {
        val cap = client.getMarketCap("BTCUSDT")
        // It can be null depending on upstream data; just ensure it doesn't throw.
        assertTrue(cap == null || cap.isNotBlank())
    }
}

