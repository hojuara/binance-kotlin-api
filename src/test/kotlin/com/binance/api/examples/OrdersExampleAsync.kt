package com.binance.api.examples

import com.binance.api.client.BinanceApiCallback
import com.binance.api.client.BinanceApiClientFactory
import com.binance.api.client.domain.TimeInForce
import com.binance.api.client.domain.account.NewOrder.Companion.limitBuy
import com.binance.api.client.domain.account.NewOrder.Companion.marketBuy
import com.binance.api.client.domain.account.NewOrderResponse
import com.binance.api.client.domain.account.Order
import com.binance.api.client.domain.account.request.*

/**
 * Examples on how to place orders, cancel them, and query account information.
 */
object OrdersExampleAsync {
    @JvmStatic
    fun main(args: Array<String>) {
        val factory = BinanceApiClientFactory.newInstance("YOUR_API_KEY", "YOUR_SECRET")
        val client = factory.newAsyncRestClient()

        // Getting list of open orders
        client.getOpenOrders(
            OrderRequest("LINKETH"),
            BinanceApiCallback { response -> println(response) })

        // Get status of a particular order
        client.getOrderStatus(
            OrderStatusRequest("LINKETH", 745262L),
            BinanceApiCallback { response: Order? -> println(response) })

        // Getting list of all orders with a limit of 10
        client.getAllOrders(
            AllOrdersRequest("LINKETH").limit(10),
            BinanceApiCallback { response -> println(response) })

        // Canceling an order
        client.cancelOrder(
            CancelOrderRequest("LINKETH", 756703L),
            BinanceApiCallback { response: CancelOrderResponse? -> println(response) })

        // Placing a test LIMIT order
        client.newOrderTest(
            limitBuy("LINKETH", TimeInForce.GTC, "1000", "0.0001"),
            BinanceApiCallback { response: Void? -> println("Test order has succeeded.") })

        // Placing a test MARKET order
        client.newOrderTest(
            marketBuy("LINKETH", "1000"),
            BinanceApiCallback { response: Void? -> println("Test order has succeeded.") })

        // Placing a real LIMIT order
        client.newOrder(
            limitBuy("LINKETH", TimeInForce.GTC, "1000", "0.0001"),
            BinanceApiCallback { response: NewOrderResponse? -> println(response) })
    }
}