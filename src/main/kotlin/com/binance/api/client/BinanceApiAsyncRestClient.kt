package com.binance.api.client

import com.binance.api.client.constant.BinanceApiConstants
import com.binance.api.client.domain.account.*
import com.binance.api.client.domain.account.request.*
import com.binance.api.client.domain.event.ListenKey
import com.binance.api.client.domain.general.Asset
import com.binance.api.client.domain.general.ExchangeInfo
import com.binance.api.client.domain.general.ServerTime
import com.binance.api.client.domain.market.*

/**
 * Binance API façade, supporting asynchronous/non-blocking access Binance's REST API.
 */
interface BinanceApiAsyncRestClient {
    // General endpoints
    /**
     * Test connectivity to the Rest API.
     */
    fun ping(callback: BinanceApiCallback<Void>)

    /**
     * Check server time.
     */
    fun getServerTime(callback: BinanceApiCallback<ServerTime>)

    /**
     * Current exchange trading rules and symbol information
     */
    fun getExchangeInfo(callback: BinanceApiCallback<ExchangeInfo>)

    /**
     * ALL supported assets and whether or not they can be withdrawn.
     */
    fun getAllAssets(callback: BinanceApiCallback<List<Asset>>)

    // Market Data endpoints
    /**
     * Get order book of a symbol (asynchronous)
     *
     * @param symbol ticker symbol (e.g. ETHBTC)
     * @param limit depth of the order book (max 100)
     * @param callback the callback that handles the response
     */
    fun getOrderBook(symbol: String, limit: Int? = null, callback: BinanceApiCallback<OrderBook>)

    /**
     * Get recent trades (up to last 500). Weight: 1
     *
     * @param symbol ticker symbol (e.g. ETHBTC)
     * @param limit of last trades (Default 500; max 1000.)
     * @param callback the callback that handles the response
     */
    fun getTrades(symbol: String, limit: Int? = null, callback: BinanceApiCallback<List<TradeHistoryItem>>)

    /**
     * Get older trades. Weight: 5
     *
     * @param symbol ticker symbol (e.g. ETHBTC)
     * @param limit of last trades (Default 500; max 1000.)
     * @param fromId TradeId to fetch from. Default gets most recent trades.
     * @param callback the callback that handles the response
     */
    fun getHistoricalTrades(
        symbol: String,
        limit: Int? = null,
        fromId: Long? = null,
        callback: BinanceApiCallback<List<TradeHistoryItem>>
    )

    /**
     * Get compressed, aggregate trades. Trades that fill at the time, from the same order, with
     * the same price will have the quantity aggregated.
     *
     * If both `startTime` and `endTime` are sent, `limit`should not
     * be sent AND the distance between `startTime` and `endTime` must be less than 24 hours.
     *
     * @param symbol symbol to aggregate (mandatory)
     * @param fromId ID to get aggregate trades from INCLUSIVE (optional)
     * @param limit Default 500; max 1000 (optional)
     * @param startTime Timestamp in ms to get aggregate trades from INCLUSIVE (optional).
     * @param endTime Timestamp in ms to get aggregate trades until INCLUSIVE (optional).
     * @param callback the callback that handles the response
     * @return a list of aggregate trades for the given symbol
     */
    fun getAggTrades(
        symbol: String,
        fromId: String? = null,
        limit: Int? = null,
        startTime: Long? = null,
        endTime: Long? = null,
        callback: BinanceApiCallback<List<AggTrade>>
    )

    /**
     * Return the most recent aggregate trades for `symbol`
     *
     * @see .getAggTrades
     */
    fun getAggTrades(symbol: String, callback: BinanceApiCallback<List<AggTrade>>)

    /**
     * Kline/candlestick bars for a symbol. Klines are uniquely identified by their open time.
     *
     * @param symbol symbol to aggregate (mandatory)
     * @param interval candlestick interval (mandatory)
     * @param limit Default 500; max 1000 (optional)
     * @param startTime Timestamp in ms to get candlestick bars from INCLUSIVE (optional).
     * @param endTime Timestamp in ms to get candlestick bars until INCLUSIVE (optional).
     * @param callback the callback that handles the response containing a candlestick bar for the given symbol and interval
     */
    fun getCandlestickBars(
        symbol: String,
        interval: CandlestickInterval,
        limit: Int? = null,
        startTime: Long? = null,
        endTime: Long? = null,
        callback: BinanceApiCallback<List<Candlestick>>
    )

    /**
     * Kline/candlestick bars for a symbol. Klines are uniquely identified by their open time.
     *
     * @see .getCandlestickBars
     */
    fun getCandlestickBars(
        symbol: String,
        interval: CandlestickInterval,
        callback: BinanceApiCallback<List<Candlestick>>
    )

    /**
     * Return current average price for a symbol (asynchronous)..
     *
     * @param symbol    symbol to aggregate (mandatory)
     * @param callback the callback that handles the response
     */
    fun getAvgPrice(symbol: String, callback: BinanceApiCallback<AvgPrice>)

    /**
     * Get 24 hour price change statistics (asynchronous).
     *
     * @param symbol ticker symbol (e.g. ETHBTC)
     * @param callback the callback that handles the response
     */
    fun get24HrPriceStatistics(symbol: String, callback: BinanceApiCallback<TickerStatistics>)

    /**
     * Get 24 hour price change statistics for all symbols (asynchronous).
     *
     * @param callback the callback that handles the response
     */
    fun getAll24HrPriceStatistics(callback: BinanceApiCallback<List<TickerStatistics>>)

    /**
     * Get Latest price for all symbols (asynchronous).
     *
     * @param callback the callback that handles the response
     */
    fun getAllPrices(callback: BinanceApiCallback<List<TickerPrice>>)

    /**
     * Get latest price for `symbol` (asynchronous).
     *
     * @param symbol ticker symbol (e.g. ETHBTC)
     * @param callback the callback that handles the response
     */
    fun getPrice(symbol: String, callback: BinanceApiCallback<TickerPrice>)

    /**
     * Get best price/qty on the order book for all symbols (asynchronous).
     *
     * @param callback the callback that handles the response
     */
    fun getBookTickers(callback: BinanceApiCallback<List<BookTicker>>)

    // Account endpoints
    /**
     * Send in a new order (asynchronous)
     *
     * @param order the new order to submit.
     * @param callback the callback that handles the response
     */
    fun newOrder(order: NewOrder, callback: BinanceApiCallback<NewOrderResponse>)

    /**
     * Test new order creation and signature/recvWindow long. Creates and validates a new order but does not send it into the matching engine.
     *
     * @param order the new TEST order to submit.
     * @param callback the callback that handles the response
     */
    fun newOrderTest(order: NewOrder, callback: BinanceApiCallback<Void>)

    /**
     * Check an order's status (asynchronous).
     *
     * @param orderStatusRequest order status request parameters
     * @param callback the callback that handles the response
     */
    fun getOrderStatus(orderStatusRequest: OrderStatusRequest, callback: BinanceApiCallback<Order>)

    /**
     * Cancel an active order (asynchronous).
     *
     * @param cancelOrderRequest order status request parameters
     * @param callback the callback that handles the response
     */
    fun cancelOrder(cancelOrderRequest: CancelOrderRequest, callback: BinanceApiCallback<CancelOrderResponse>)

    /**
     * Get all open orders on a symbol (asynchronous).
     *
     * @param orderRequest order request parameters
     * @param callback the callback that handles the response
     */
    fun getOpenOrders(orderRequest: OrderRequest, callback: BinanceApiCallback<List<Order>>)

    /**
     * Get all account orders; active, canceled, or filled.
     *
     * @param orderRequest order request parameters
     * @param callback the callback that handles the response
     */
    fun getAllOrders(orderRequest: AllOrdersRequest, callback: BinanceApiCallback<List<Order>>)

    /**
     * Get current account information (async).
     */
    fun getAccount(omitZeroBalances: Boolean? = null, recvWindow: Long? = BinanceApiConstants.DEFAULT_RECEIVING_WINDOW, callback: BinanceApiCallback<Account>)

    /**
     * Get trades for a specific account and symbol.
     *
     * @param symbol symbol to get trades from
     * @param limit default 500; max 1000
     * @param fromId TradeId to fetch from. Default gets most recent trades.
     * @param callback the callback that handles the response with a list of trades
     */
    fun getMyTrades(
        symbol: String,
        limit: Int? = null,
        fromId: Long? = null,
        recvWindow: Long? = null,
        timestamp: Long? = null,
        callback: BinanceApiCallback<List<Trade>>
    )

    /**
     * Submit a withdraw request.
     *
     * Enable Withdrawals option has to be active in the API settings.
     *
     * @param asset asset symbol to withdraw
     * @param address address to withdraw to
     * @param amount amount to withdraw
     * @param name description/alias of the address
     * @param addressTag Secondary address identifier for coins like XRP,XMR etc.
     */
    fun withdraw(
        asset: String,
        address: String,
        amount: String,
        name: String? = null,
        addressTag: String? = null,
        callback: BinanceApiCallback<WithdrawResult>
    )

    /**
     * Fetch account deposit history.
     *
     * @param callback the callback that handles the response and returns the deposit history
     */
    fun getDepositHistory(asset: String, callback: BinanceApiCallback<Array<Deposit>>)

    /**
     * Fetch account withdraw history.
     *
     * @param callback the callback that handles the response and returns the withdraw history
     */
    fun getWithdrawHistory(asset: String, callback: BinanceApiCallback<Array<Withdraw>>)

    /**
     * Fetch deposit address.
     *
     * @param callback the callback that handles the response and returns the deposit address
     */
    fun getDepositAddress(asset: String, callback: BinanceApiCallback<DepositAddress>)

    /**
     * Convert dust assets to BNB (async)
     *
     * @param assets
     */
    fun convertDust(assets: List<String>, callback: BinanceApiCallback<DustConversionInfo>)

    /**
     * Return market cap of symbol
     *
     * @param symbol
     * @return
     */
    fun getMarketCap(symbol: String, callback: BinanceApiCallback<String?>)
}