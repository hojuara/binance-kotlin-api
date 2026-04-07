package com.binance.api.client.impl

import com.binance.api.client.BinanceApiAsyncRestClient
import com.binance.api.client.BinanceApiCallback
import com.binance.api.client.domain.account.*
import com.binance.api.client.domain.account.request.*
import com.binance.api.client.domain.general.Asset
import com.binance.api.client.domain.general.ExchangeInfo
import com.binance.api.client.domain.general.ServerTime
import com.binance.api.client.domain.market.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Asynchronous Binance REST client backed by [BinanceApiRestClientImpl] and Spring RestClient.
 */
class BinanceApiAsyncRestClientImpl(
    apiKey: String?,
    secret: String?
) : BinanceApiAsyncRestClient {

    private val syncClient: BinanceApiRestClientImpl = BinanceApiRestClientImpl(apiKey, secret)
    private val executor: ExecutorService = Executors.newCachedThreadPool()

    private fun <T> submit(callback: BinanceApiCallback<T>, block: () -> T) {
        executor.submit {
            try {
                val result = block()
                callback.onResponse(result)
            } catch (ex: Throwable) {
                callback.onFailure(ex)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun BinanceApiCallback<Void>.trigger() = (this as BinanceApiCallback<Any?>).onResponse(null)

    private fun submitVoid(callback: BinanceApiCallback<Void>, block: () -> Unit) {
        executor.submit {
            try {
                block()
                callback.trigger()
            } catch (ex: Throwable) {
                callback.onFailure(ex)
            }
        }
    }

    // General endpoints

    override fun ping(callback: BinanceApiCallback<Void>) =
        submitVoid(callback) { syncClient.ping() }

    override fun getServerTime(callback: BinanceApiCallback<ServerTime>) =
        submit(callback) {
            ServerTime(syncClient.serverTime)
        }

    override fun getExchangeInfo(callback: BinanceApiCallback<ExchangeInfo>) =
        submit(callback) { syncClient.exchangeInfo }

    override fun getAllAssets(callback: BinanceApiCallback<List<Asset>>) =
        submit(callback) { syncClient.allAssets }

    // Market Data endpoints

    override fun getOrderBook(symbol: String, limit: Int, callback: BinanceApiCallback<OrderBook>) =
        submit(callback) { syncClient.getOrderBook(symbol, limit) }

    override fun getTrades(
        symbol: String,
        limit: Int,
        callback: BinanceApiCallback<List<TradeHistoryItem>>
    ) = submit(callback) { syncClient.getTrades(symbol, limit) }

    override fun getHistoricalTrades(
        symbol: String,
        limit: Int,
        fromId: Long,
        callback: BinanceApiCallback<List<TradeHistoryItem>>
    ) = submit(callback) { syncClient.getHistoricalTrades(symbol, limit, fromId) }

    override fun getAggTrades(
        symbol: String,
        fromId: String,
        limit: Int,
        startTime: Long,
        endTime: Long,
        callback: BinanceApiCallback<List<AggTrade>>
    ) = submit(callback) { syncClient.getAggTrades(symbol, fromId, limit, startTime, endTime) }

    override fun getAggTrades(symbol: String, callback: BinanceApiCallback<List<AggTrade>>) =
        submit(callback) { syncClient.getAggTrades(symbol) }

    override fun getCandlestickBars(
        symbol: String,
        interval: CandlestickInterval,
        limit: Int,
        startTime: Long,
        endTime: Long,
        callback: BinanceApiCallback<List<Candlestick>>
    ) = submit(callback) { syncClient.getCandlestickBars(symbol, interval, limit, startTime, endTime) }

    override fun getCandlestickBars(
        symbol: String,
        interval: CandlestickInterval,
        callback: BinanceApiCallback<List<Candlestick>>
    ) = submit(callback) { syncClient.getCandlestickBars(symbol, interval) }

    override fun get24HrPriceStatistics(
        symbol: String,
        callback: BinanceApiCallback<TickerStatistics>
    ) = submit(callback) { syncClient.get24HrPriceStatistics(symbol) }

    override fun getAll24HrPriceStatistics(
        callback: BinanceApiCallback<List<TickerStatistics>>
    ) = submit(callback) { syncClient.all24HrPriceStatistics }

    override fun getAllPrices(callback: BinanceApiCallback<List<TickerPrice>>) =
        submit(callback) { syncClient.allPrices }

    override fun getPrice(symbol: String, callback: BinanceApiCallback<TickerPrice>) =
        submit(callback) { syncClient.getPrice(symbol) }

    override fun getBookTickers(callback: BinanceApiCallback<List<BookTicker>>) =
        submit(callback) { syncClient.bookTickers }

    // Account endpoints

    override fun newOrder(order: NewOrder, callback: BinanceApiCallback<NewOrderResponse>) =
        submit(callback) { syncClient.newOrder(order) }

    override fun newOrderTest(order: NewOrder, callback: BinanceApiCallback<Void>) =
        submitVoid(callback) { syncClient.newOrderTest(order) }

    override fun getOrderStatus(
        orderStatusRequest: OrderStatusRequest,
        callback: BinanceApiCallback<Order>
    ) = submit(callback) { syncClient.getOrderStatus(orderStatusRequest) }

    override fun cancelOrder(
        cancelOrderRequest: CancelOrderRequest,
        callback: BinanceApiCallback<CancelOrderResponse>
    ) = submit(callback) { syncClient.cancelOrder(cancelOrderRequest) }

    override fun getOpenOrders(
        orderRequest: OrderRequest,
        callback: BinanceApiCallback<List<Order>>
    ) = submit(callback) { syncClient.getOpenOrders(orderRequest) }

    override fun getAllOrders(
        orderRequest: AllOrdersRequest,
        callback: BinanceApiCallback<List<Order>>
    ) = submit(callback) { syncClient.getAllOrders(orderRequest) }

    override fun getAccount(
        recvWindow: Long,
        timestamp: Long,
        callback: BinanceApiCallback<Account>
    ) = submit(callback) { syncClient.getAccount(recvWindow, timestamp) }

    override fun getAccount(callback: BinanceApiCallback<Account>) =
        submit(callback) { syncClient.account }

    override fun getMyTrades(
        symbol: String,
        limit: Int,
        fromId: Long,
        recvWindow: Long,
        timestamp: Long,
        callback: BinanceApiCallback<List<Trade>>
    ) = submit(callback) { syncClient.getMyTrades(symbol, limit, fromId, recvWindow, timestamp) }

    override fun getMyTrades(
        symbol: String,
        limit: Int,
        callback: BinanceApiCallback<List<Trade>>
    ) = submit(callback) { syncClient.getMyTrades(symbol, limit) }

    override fun getMyTrades(
        symbol: String,
        callback: BinanceApiCallback<List<Trade>>
    ) = submit(callback) { syncClient.getMyTrades(symbol) }

    override fun withdraw(
        asset: String,
        address: String,
        amount: String,
        name: String?,
        addressTag: String?,
        callback: BinanceApiCallback<WithdrawResult>
    ) = submit(callback) { syncClient.withdraw(asset, address, amount, name, addressTag) }

    override fun getDepositHistory(
        asset: String,
        callback: BinanceApiCallback<Array<Deposit>>
    ) = submit(callback) { syncClient.getDepositHistory(asset) }

    override fun getWithdrawHistory(
        asset: String,
        callback: BinanceApiCallback<Array<Withdraw>>
    ) = submit(callback) { syncClient.getWithdrawHistory(asset) }

    override fun getDepositAddress(
        asset: String,
        callback: BinanceApiCallback<DepositAddress>
    ) = submit(callback) { syncClient.getDepositAddress(asset) }
}

