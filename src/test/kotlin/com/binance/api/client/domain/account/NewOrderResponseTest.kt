package com.binance.api.client.domain.account

import com.binance.api.client.domain.OrderSide
import com.binance.api.client.domain.OrderStatus
import com.binance.api.client.domain.OrderType
import com.binance.api.client.domain.TimeInForce
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertContains

/**
 * Test for [NewOrderResponse]
 */
class NewOrderResponseTest {

    private lateinit var newOrderResponse: NewOrderResponse
    private lateinit var trade: Trade

    @Test
    fun `should handle toString with null fills`() {
        val newOrderResponse = createResponse()
        val toString = newOrderResponse.toString()

        assertTrue(toString.contains("fills="), "Deve conter o campo 'fills' no toString")
    }

    @Test
    fun `should handle toString with no fills`() {
        val newOrderResponse = createResponse(emptyList())
        val toString = newOrderResponse.toString()

        assertContains(toString, "fills=\\[]".toRegex(), "Deve mostrar lista vazia para fills")
    }

    @Test
    fun `should handle toString with fills`() {
        val trade = Trade(
            symbol = "BNB",
            id = 123,
            orderId = 123,
            price = "1.00000000",
            qty = "1.00000000",
            quoteQty = "1.00000000",
            commission = "1.00000000",
            commissionAsset = "1.00000000",
            time = 123,
            isBuyer = true,
            isMaker = false,
            isBestMatch = false
        )
        val newOrderResponse = createResponse(listOf(trade))
        val toString = newOrderResponse.toString()

        assertContains(toString, "fills=\\[Trade\\[id=123,".toRegex(), "Deve conter os detalhes do trade nos fills")
    }

    private fun createResponse(fills: List<Trade>? = null) = NewOrderResponse(
        symbol = "BNB",
        orderId = 123,
        orderListId = -1,
        clientOrderId = "123",
        transactTime = 123,
        price = "1.00000000",
        origQty = "1.00000000",
        executedQty = "1.00000000",
        cummulativeQuoteQty = "1.00000000",
        status = OrderStatus.NEW,
        timeInForce = TimeInForce.GTC,
        type = OrderType.MARKET,
        side = OrderSide.BUY,
        fills = fills
    )
}