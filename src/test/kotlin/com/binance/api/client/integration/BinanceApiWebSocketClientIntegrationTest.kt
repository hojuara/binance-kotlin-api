package com.binance.api.client.integration

import com.binance.api.client.BinanceApiCallback
import com.binance.api.client.BinanceApiWebSocketClient
import com.binance.api.client.domain.event.AggTradeEvent
import com.binance.api.client.domain.event.BookTickerEvent
import com.binance.api.client.domain.event.CandlestickEvent
import com.binance.api.client.domain.event.DepthEvent
import com.binance.api.client.domain.event.UserDataUpdateEvent
import com.binance.api.client.domain.event.AllMarketTickersEvent
import com.binance.api.client.domain.event.BookDepthEvent
import com.binance.api.client.domain.market.CandlestickInterval
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BinanceApiWebSocketClientIntegrationTest {

    private lateinit var client: BinanceApiWebSocketClient

    @BeforeAll
    fun setUp() {
        BinanceIntegrationTestSupport.requireApiKeysOrSkip()
        client = BinanceIntegrationTestSupport.newWebSocketClient()
    }

    @Test
    fun `depth stream should deliver at least one event`() {
        val latch = CountDownLatch(1)
        val eventRef = AtomicReference<DepthEvent?>()
        val failureRef = AtomicReference<Throwable?>()

        val closeable = client.onDepthEvent(
            "btcusdt",
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

        try {
            val ok = latch.await(15, TimeUnit.SECONDS)
            assertTrue(ok, "Timed out waiting for first depth event")
            assertNull(failureRef.get())
            val event = eventRef.get()
            assertNotNull(event)
            assertFalse(event!!.eventType.isNullOrBlank())
            assertTrue(event.eventTime > 0)
            assertFalse(event.symbol.isNullOrBlank())
            assertTrue(event.finalUpdateId > 0)
        } finally {
            closeable.close()
        }
    }

    @Test
    fun `candlestick stream should deliver at least one event`() {
        val latch = CountDownLatch(1)
        val eventRef = AtomicReference<CandlestickEvent?>()
        val failureRef = AtomicReference<Throwable?>()

        val closeable = client.onCandlestickEvent(
            "btcusdt",
            CandlestickInterval.ONE_MINUTE,
            object : BinanceApiCallback<CandlestickEvent> {
                override fun onResponse(response: CandlestickEvent) {
                    eventRef.set(response)
                    latch.countDown()
                }

                override fun onFailure(cause: Throwable) {
                    failureRef.set(cause)
                    latch.countDown()
                }
            }
        )

        try {
            val ok = latch.await(15, TimeUnit.SECONDS)
            assertTrue(ok, "Timed out waiting for first candlestick event")
            assertNull(failureRef.get())
            val event = eventRef.get()
            assertNotNull(event)
            assertFalse(event!!.eventType.isBlank())
            assertTrue(event.eventTime > 0)
            assertTrue(event.openTime > 0)
            assertTrue(event.closeTime > 0)
            assertFalse(event.symbol.isBlank())
            assertFalse(event.open.isBlank())
        } finally {
            closeable.close()
        }
    }

    @Test
    fun `aggTrade stream should deliver at least one event`() {
        val latch = CountDownLatch(1)
        val eventRef = AtomicReference<AggTradeEvent?>()
        val failureRef = AtomicReference<Throwable?>()

        val closeable = client.onAggTradeEvent(
            "btcusdt",
            object : BinanceApiCallback<AggTradeEvent> {
                override fun onResponse(response: AggTradeEvent) {
                    eventRef.set(response)
                    latch.countDown()
                }

                override fun onFailure(cause: Throwable) {
                    failureRef.set(cause)
                    latch.countDown()
                }
            }
        )

        try {
            val ok = latch.await(15, TimeUnit.SECONDS)
            assertTrue(ok, "Timed out waiting for first aggTrade event")
            assertNull(failureRef.get())

            val event = eventRef.get()
            assertNotNull(event)
            assertEquals("btcusdt".uppercase(), event!!.symbol.uppercase())
            assertFalse(event.eventType.isBlank())
            assertTrue(event.eventTime > 0)
            assertTrue(event.tradeTime > 0)
            assertFalse(event.price.isBlank())
            assertFalse(event.quantity.isBlank())
        } finally {
            closeable.close()
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

        try {
            val ok = latch.await(20, TimeUnit.SECONDS)
            assertTrue(ok, "Timed out waiting for first userData event")
            assertNull(failureRef.get())
            assertNotNull(eventRef.get())
        } finally {
            closeable.close()
        }
    }

    @Test
    fun `allMarketTickers stream should deliver at least one event`() {
        val latch = CountDownLatch(1)
        val eventRef = AtomicReference<List<AllMarketTickersEvent>?>()
        val failureRef = AtomicReference<Throwable?>()

        val closeable = client.onAllMarketTickersEvent(
            object : BinanceApiCallback<List<AllMarketTickersEvent>> {
                override fun onResponse(response: List<AllMarketTickersEvent>) {
                    eventRef.set(response)
                    latch.countDown()
                }

                override fun onFailure(cause: Throwable) {
                    failureRef.set(cause)
                    latch.countDown()
                }
            }
        )

        try {
            val ok = latch.await(60, TimeUnit.SECONDS)
            assertTrue(ok, "Timed out waiting for first allMarketTickers event")
            assertNull(failureRef.get())
            val list = eventRef.get()
            assertNotNull(list)
            assertTrue(list!!.isNotEmpty())
        } finally {
            closeable.close()
        }
    }

    @Test
    fun `partialDepth stream should deliver at least one event`() {
        val latch = CountDownLatch(1)
        val eventRef = AtomicReference<BookDepthEvent?>()
        val failureRef = AtomicReference<Throwable?>()

        val closeable = client.onPartialDepthEvent(
            "btcusdt",
            5,
            object : BinanceApiCallback<BookDepthEvent> {
                override fun onResponse(response: BookDepthEvent) {
                    eventRef.set(response)
                    latch.countDown()
                }

                override fun onFailure(cause: Throwable) {
                    failureRef.set(cause)
                    latch.countDown()
                }
            }
        )

        try {
            val ok = latch.await(15, TimeUnit.SECONDS)
            assertTrue(ok, "Timed out waiting for first partialDepth event")
            assertNull(failureRef.get())
            val event = eventRef.get()
            assertNotNull(event)
            assertTrue(event!!.lastUpdateId > 0)
        } finally {
            closeable.close()
        }
    }

    @Test
    fun `bookTicker stream should deliver at least one event and match expected fields`() {
        val latch = CountDownLatch(1)
        val eventRef = AtomicReference<BookTickerEvent?>()
        val failureRef = AtomicReference<Throwable?>()

        val closeable = client.onBookTickerEvent(
            "btcusdt",
            object : BinanceApiCallback<BookTickerEvent> {
                override fun onResponse(response: BookTickerEvent) {
                    eventRef.set(response)
                    latch.countDown()
                }

                override fun onFailure(cause: Throwable) {
                    failureRef.set(cause)
                    latch.countDown()
                }
            }
        )

        try {
            val ok = latch.await(15, TimeUnit.SECONDS)
            assertTrue(ok, "Timed out waiting for first bookTicker event")
            assertNull(failureRef.get())

            val event = eventRef.get()
            assertNotNull(event)
            assertEquals("BTCUSDT", event!!.symbol)
            assertTrue(event.lastUpdateId > 0)
            assertFalse(event.bidPrice.isNullOrBlank())
            assertFalse(event.askPrice.isNullOrBlank())
        } finally {
            closeable.close()
        }
    }
}

