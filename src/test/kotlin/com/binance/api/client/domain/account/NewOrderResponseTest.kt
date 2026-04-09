package com.binance.api.client.domain.account

import com.binance.api.client.domain.OrderSide
import com.binance.api.client.domain.OrderStatus
import com.binance.api.client.domain.OrderType
import com.binance.api.client.domain.TimeInForce
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertContains

/**
 * Test for [NewOrderResponse]
 */
class NewOrderResponseTest {

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
        val trade = TradeFill(
            tradeId = 123,
            commissionAsset = "BNB",
            price = "1.00000000",
            qty = "1.00000000",
            commission = "1.00000000"
        )
        val newOrderResponse = createResponse(listOf(trade))
        val toString = newOrderResponse.toString()

        assertContains(toString, "fills=\\[TradeFill\\(tradeId=123,".toRegex(), "Deve conter os detalhes do trade nos fills")
    }

    private fun createResponse(fills: List<TradeFill>? = null) = NewOrderResponse(
        symbol = "BNB",
        orderId = 123,
        clientOrderId = "123",
        transactTime = 123,
        price = "1.00000000",
        origQty = "1.00000000",
        executedQty = "1.00000000",
        origQuoteOrderQty = "1.00000000",
        cummulativeQuoteQty = "1.00000000",
        status = OrderStatus.NEW,
        timeInForce = TimeInForce.GTC,
        type = OrderType.MARKET,
        side = OrderSide.BUY,
        workingTime = 123,
        fills = fills
    )
}