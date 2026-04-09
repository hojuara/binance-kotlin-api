package com.binance.api.domain.event

import com.binance.api.client.domain.*
import com.binance.api.client.domain.event.UserDataUpdateEvent
import com.binance.api.client.domain.event.UserDataUpdateEvent.UserDataUpdateEventType
import com.binance.api.client.utils.JsonMapperUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import java.io.IOException

/**
 * Tests that JSON responses from the stream API are converted to the
 * appropriate object.
 */
class UserDataUpdateEventDeserializerTest {

    private val mapper = JsonMapperUtils.getInstance()

    @Test
    fun `should deserialize account update event`() {
        val accountUpdateJson = """
            {
                "subscriptionId" : 0,
                "event" : {
                    "e" : "outboundAccountPosition",
                    "E" : 1775700812111,
                    "u" : 1775700812111,
                    "B" : [
                        {
                            "a" : "BTC",
                            "f" : "0.00045282",
                            "l" : "0.00000000"
                        },
                        {
                            "a" : "ETH",
                            "f" : "0.00000000",
                            "l" : "0.00000000"
                        }
                    ]
                }
            }
        """.trimIndent()

        try {
            val event = mapper.readValue(accountUpdateJson, UserDataUpdateEvent::class.java)

            assertEquals(UserDataUpdateEventType.ACCOUNT_UPDATE, event.eventType)
            assertEquals(1775700812111L, event.eventTime)

            val accountUpdateEvent = event.accountUpdateEvent!!
            accountUpdateEvent.balances.forEach { assetBalance ->
                if (assetBalance.asset == "BTC") {
                    assertEquals("0.00045282", assetBalance.free)
                } else {
                    assertEquals("0.00000000", assetBalance.free)
                }
                assertEquals("0.00000000", assetBalance.locked)
            }
        } catch (e: IOException) {
            fail("Erro na desserialização do AccountUpdateEvent: ${e.message}")
        }
    }

    @Test
    fun `should deserialize order update event`() {
        val orderUpdateEventJson = """
            {
                "subscriptionId" : 0,
                "event" : {
                    "e": "executionReport",
                    "E": 1,
                    "s": "NEOETH",
                    "c": "XXX",
                    "S": "BUY",
                    "o": "LIMIT",
                    "f": "GTC",
                    "q": "1000.00000000",
                    "p": "0.00010000",
                    "P": "0.00000000",
                    "F": "0.00000000",
                    "g": -1,
                    "C": "5yairWLqfzbusOUdPyG712",
                    "x": "CANCELED",
                    "X": "CANCELED",
                    "r": "NONE",
                    "i": 123456,
                    "l": "0.00000000",
                    "z": "0.00000000",
                    "L": "0.00000000",
                    "n": "0",
                    "N": null,
                    "T": 1,
                    "t": -1,
                    "I": 1,
                    "w": false,
                    "m": false,
                    "M": false
                }
            }
        """.trimIndent()

        try {
            val event = mapper.readValue(orderUpdateEventJson, UserDataUpdateEvent::class.java)

            assertEquals(UserDataUpdateEventType.ORDER_TRADE_UPDATE, event.eventType)
            assertEquals(1L, event.eventTime)

            event.orderTradeUpdateEvent.let {
                val update = it!!
                assertEquals("NEOETH", update.symbol)
                assertEquals("XXX", update.newClientOrderId)
                assertEquals(OrderSide.BUY, update.side)
                assertEquals(OrderType.LIMIT, update.type)
                assertEquals(TimeInForce.GTC, update.timeInForce)
                assertEquals("1000.00000000", update.originalQuantity)
                assertEquals("0.00010000", update.price)
                assertEquals(ExecutionType.CANCELED, update.executionType)
                assertEquals(OrderStatus.CANCELED, update.orderStatus)
                assertEquals(OrderRejectReason.NONE, update.orderRejectReason)
                assertEquals(123456L, update.orderId)
                assertEquals(1L, update.orderTradeTime)
            }
        } catch (e: IOException) {
            fail("Erro na desserialização do OrderTradeUpdateEvent: ${e.message}")
        }
    }
}