package com.binance.api.examples

import com.binance.api.client.BinanceApiClientFactory
import com.binance.api.client.domain.TimeInForce
import com.binance.api.client.domain.account.NewOrder.Companion.limitBuy
import com.binance.api.client.domain.account.NewOrder.Companion.marketBuy
import com.binance.api.client.domain.account.NewOrderResponseType
import com.binance.api.client.domain.account.request.AllOrdersRequest
import com.binance.api.client.domain.account.request.CancelOrderRequest
import com.binance.api.client.domain.account.request.OrderRequest
import com.binance.api.client.domain.account.request.OrderStatusRequest
import com.binance.api.client.exception.BinanceApiException

/**
 * Examples on how to place orders, cancel them, and query account information.
 */
object OrdersExample {
    @JvmStatic
    fun main(args: Array<String>) {
        val factory = BinanceApiClientFactory.newInstance("YOUR_API_KEY", "YOUR_SECRET")
        val client = factory.newRestClient()

        // Getting list of open orders
        val openOrders = client.getOpenOrders(OrderRequest("LINKETH"))
        println(openOrders)

        // Getting list of all orders with a limit of 10
        val allOrders = client.getAllOrders(AllOrdersRequest("LINKETH").limit(10))
        println(allOrders)

        // Get status of a particular order
        val order = client.getOrderStatus(OrderStatusRequest("LINKETH", 751698L))
        println(order)

        // Canceling an order
        try {
            val cancelOrderResponse = client.cancelOrder(CancelOrderRequest("LINKETH", 756762L))
            println(cancelOrderResponse)
        } catch (e: BinanceApiException) {
            println(e.error!!.msg)
        }

        // Placing a test LIMIT order
        client.newOrderTest(limitBuy("LINKETH", TimeInForce.GTC, "1000", "0.0001"))

        // Placing a test MARKET order
        client.newOrderTest(marketBuy("LINKETH", "1000"))

        // Placing a real LIMIT order
        val newOrderResponse = client.newOrder(
            limitBuy("LINKETH", TimeInForce.GTC, "1000", "0.0001").apply {
                newOrderRespType = NewOrderResponseType.FULL
            }
        )
        println(newOrderResponse)
    }
}