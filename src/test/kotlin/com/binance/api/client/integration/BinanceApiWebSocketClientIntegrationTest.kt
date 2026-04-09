package com.binance.api.client.integration

import com.binance.api.client.BinanceApiCallback
import com.binance.api.client.BinanceApiWebSocketClient
import com.binance.api.client.domain.event.*
import com.binance.api.client.domain.market.CandlestickInterval
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertNotNull
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BinanceApiWebSocketClientIntegrationTest {

    private companion object {
        const val SYMBOL = "BTCUSDT"
        val SYMBOLS = arrayOf("BTCUSDT", "ETHBTC")
    }

    private lateinit var client: BinanceApiWebSocketClient

    @BeforeAll
    fun setUp() {
        // BinanceIntegrationTestSupport.requireApiKeysOrSkip()
        client = BinanceIntegrationTestSupport.newWebSocketClient()
    }

    @Test
    fun `depth stream should deliver at least one event`() {
        val latch = CountDownLatch(1)
        val eventRef = AtomicReference<DepthEvent?>()
        val failureRef = AtomicReference<Throwable?>()

        val closeable = client.onDepthEvent(
            SYMBOL,
            object : BinanceApiCallback<DepthEvent> {
                override fun onResponse(response: DepthEvent) {
                    eventRef.set(response)
                    latch.countDown()
                }

                override fun onFailure(cause: Throwable) {
                    failureRef.set(cause)
                    latch.countDown()
                }
            }
        )

        closeable.use {
            val ok = latch.await(15, TimeUnit.SECONDS)
            assertTrue(ok, "Timed out waiting for first depth event")
            assertNull(failureRef.get())
            val event = eventRef.get()
            assertNotNull(event)
            assertTrue(event.lastUpdateId > 0)
            assertTrue(event.bids.isNotEmpty())
            assertTrue(event.asks.isNotEmpty())
        }
    }

    @Test
    fun `candlestick stream should deliver at least one event`() {
        val latch = CountDownLatch(1)
        val eventRef = AtomicReference<Array<CandlestickEvent>?>()
        val failureRef = AtomicReference<Throwable?>()

        val closeable = client.onCandlestickEvent(
            SYMBOL,
            CandlestickInterval.ONE_MINUTE,
            object : BinanceApiCallback<Array<CandlestickEvent>> {
                override fun onResponse(response: Array<CandlestickEvent>) {
                    eventRef.set(response)
                    latch.countDown()
                }

                override fun onFailure(cause: Throwable) {
                    failureRef.set(cause)
                    latch.countDown()
                }
            }
        )

        closeable.use {
            val ok = latch.await(15, TimeUnit.SECONDS)
            assertTrue(ok, "Timed out waiting for first candlestick event")
            assertNull(failureRef.get())
            val event = eventRef.get()
            assertNotNull(event)
            assertTrue(event.isNotEmpty())
            val kline = event.first()
            assertTrue(kline.openTime > 0)
            assertTrue(kline.open.isNotBlank())
            assertTrue(kline.high.isNotBlank())
            assertTrue(kline.low.isNotBlank())
            assertTrue(kline.close.isNotBlank())
            assertTrue(kline.volume.isNotBlank())
            assertTrue(kline.closeTime > 0)
            assertTrue(kline.quoteAssetVolume.isNotBlank())
            assertTrue(kline.numberOfTrades >= 0)
            assertTrue(kline.takerBuyBaseAssetVolume.isNotBlank())
            assertTrue(kline.takerBuyQuoteAssetVolume.isNotBlank())
        }
    }

    @Test
    fun `aggTrade stream should deliver at least one event`() {
        val latch = CountDownLatch(1)
        val eventRef = AtomicReference<Array<AggTradeEvent>?>()
        val failureRef = AtomicReference<Throwable?>()

        val closeable = client.onAggTradeEvent(
            SYMBOL,
            object : BinanceApiCallback<Array<AggTradeEvent>> {
                override fun onResponse(response: Array<AggTradeEvent>) {
                    eventRef.set(response)
                    latch.countDown()
                }

                override fun onFailure(cause: Throwable) {
                    failureRef.set(cause)
                    latch.countDown()
                }
            }
        )

        closeable.use {
            val ok = latch.await(15, TimeUnit.SECONDS)
            assertTrue(ok, "Timed out waiting for first aggTrade event")
            assertNull(failureRef.get())

            val event = eventRef.get()
            assertNotNull(event)
            assertTrue(event.isNotEmpty())
            val aggTrade = event.first()
            assertTrue(aggTrade.aggregatedTradeId > 0)
            assertTrue(aggTrade.price.isNotBlank())
            assertTrue(aggTrade.quantity.isNotBlank())
            assertTrue(aggTrade.firstBreakdownTradeId > 0)
            assertTrue(aggTrade.lastBreakdownTradeId > 0)
            assertTrue(aggTrade.tradeTime > 0)
            assertNotNull(aggTrade.isBuyerMaker)
            assertNotNull(aggTrade.wasBestPriceMatch)
        }
    }

    // @Test
    fun `userData stream should deliver at least one event (disabled by default)`() {
        val latch = CountDownLatch(1)
        val eventRef = AtomicReference<UserDataUpdateEvent?>()
        val failureRef = AtomicReference<Throwable?>()

        val closeable = client.onUserDataUpdateEvent(
            object : BinanceApiCallback<UserDataUpdateEvent> {
                override fun onResponse(response: UserDataUpdateEvent) {
                    eventRef.set(response)
                    latch.countDown()
                }

                override fun onFailure(cause: Throwable) {
                    failureRef.set(cause)
                    latch.countDown()
                }
            }
        )

        closeable.use {
            val ok = latch.await(60, TimeUnit.SECONDS)
            assertTrue(ok, "Timed out waiting for first userData event")
            assertNull(failureRef.get())
            assertNotNull(eventRef.get())
        }
    }

    @Test
    fun `bookTicker stream should deliver at least one event and match expected fields`() {
        val latch = CountDownLatch(1)
        val eventRef = AtomicReference<Array<BookTickerEvent>?>()
        val failureRef = AtomicReference<Throwable?>()

        val closeable = client.onBookTickerEvent(
            SYMBOLS,
            object : BinanceApiCallback<Array<BookTickerEvent>> {
                override fun onResponse(response: Array<BookTickerEvent>) {
                    eventRef.set(response)
                    latch.countDown()
                }

                override fun onFailure(cause: Throwable) {
                    failureRef.set(cause)
                    latch.countDown()
                }
            }
        )

        closeable.use {
            val ok = latch.await(15, TimeUnit.SECONDS)
            assertTrue(ok, "Timed out waiting for first bookTicker event")
            assertNull(failureRef.get())

            val event = eventRef.get()
            assertNotNull(event)
            assertTrue(event.isNotEmpty())
            val bookTicker = event.first()
            assertTrue(SYMBOLS.contains(bookTicker.symbol))
            assertTrue(bookTicker.bidPrice.isNotBlank())
            assertTrue(bookTicker.bidQty.isNotBlank())
            assertTrue(bookTicker.askPrice.isNotBlank())
            assertTrue(bookTicker.askQty.isNotBlank())
        }
    }
}

