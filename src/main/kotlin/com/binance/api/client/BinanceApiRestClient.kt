package com.binance.api.client

import com.binance.api.client.domain.account.*
import com.binance.api.client.domain.account.request.*
import com.binance.api.client.domain.general.Asset
import com.binance.api.client.domain.general.ExchangeInfo
import com.binance.api.client.domain.market.*

/**
 * Binance API façade, supporting synchronous/blocking access Binance's REST
 * API.
 */
interface BinanceApiRestClient {
    // General endpoints
    /**
     * Test connectivity to the Rest API.
     */
    fun ping()

    /**
     * Test connectivity to the Rest API and get the current server time.
     *
     * @return current server time.
     */
    val serverTime: Long

    /**
     * @return Current exchange trading rules and symbol information
     */
    val exchangeInfo: ExchangeInfo

    /**
     * @return All the supported assets and whether or not they can be withdrawn.
     */
    val allAssets: List<Asset>

    // Market Data endpoints
    /**
     * Get order book of a symbol.
     *
     * @param symbol ticker symbol (e.g. ETHBTC)
     * @param limit  depth of the order book (max 100)
     */
    fun getOrderBook(symbol: String, limit: Int): OrderBook

    /**
     * Get recent trades (up to last 500). Weight: 1
     *
     * @param symbol ticker symbol (e.g. ETHBTC)
     * @param limit  of last trades (Default 500; max 1000.)
     */
    fun getTrades(symbol: String, limit: Int): List<TradeHistoryItem>

    /**
     * Get older trades. Weight: 5
     *
     * @param symbol ticker symbol (e.g. ETHBTC)
     * @param limit  of last trades (Default 500; max 1000.)
     * @param fromId TradeId to fetch from. Default gets most recent trades.
     */
    fun getHistoricalTrades(symbol: String, limit: Int, fromId: Long): List<TradeHistoryItem>

    /**
     * Get compressed, aggregate trades. Trades that fill at the time, from the same
     * order, with
     * the same price will have the quantity aggregated.
     *
     * If both `startTime` and `endTime` are sent,
     * `limit`should not
     * be sent AND the distance between `startTime` and
     * `endTime` must be less than 24 hours.
     *
     * @param symbol    symbol to aggregate (mandatory)
     * @param fromId    ID to get aggregate trades from INCLUSIVE (optional)
     * @param limit     Default 500; max 1000 (optional)
     * @param startTime Timestamp in ms to get aggregate trades from INCLUSIVE
     * (optional).
     * @param endTime   Timestamp in ms to get aggregate trades until INCLUSIVE
     * (optional).
     * @return a list of aggregate trades for the given symbol
     */
    fun getAggTrades(
        symbol: String,
        fromId: String?,
        limit: Int?,
        startTime: Long?,
        endTime: Long?
    ): List<AggTrade>

    /**
     * Return the most recent aggregate trades for `symbol`
     *
     * @see .getAggTrades
     */
    fun getAggTrades(symbol: String): List<AggTrade>

    /**
     * Kline/candlestick bars for a symbol. Klines are uniquely identified by their
     * open time.
     *
     * @param symbol    symbol to aggregate (mandatory)
     * @param interval  candlestick interval (mandatory)
     * @param limit     Default 500; max 1000 (optional)
     * @param startTime Timestamp in ms to get candlestick bars from INCLUSIVE
     * (optional).
     * @param endTime   Timestamp in ms to get candlestick bars until INCLUSIVE
     * (optional).
     * @return a candlestick bar for the given symbol and interval
     */
    fun getCandlestickBars(
        symbol: String,
        interval: CandlestickInterval,
        limit: Int?,
        startTime: Long?,
        endTime: Long?
    ): List<Candlestick>

    /**
     * Return current average price for a symbol.
     *
     * @param symbol    symbol to aggregate (mandatory)
     * @return a average price information for symbol
     */
    fun getAvgPrice(symbol: String): AvgPrice

    /**
     * Kline/candlestick bars for a symbol. Klines are uniquely identified by their
     * open time.
     *
     * @see .getCandlestickBars
     */
    fun getCandlestickBars(symbol: String, interval: CandlestickInterval): List<Candlestick>

    /**
     * Get 24 hour price change statistics.
     *
     * @param symbol ticker symbol (e.g. ETHBTC)
     */
    fun get24HrPriceStatistics(symbol: String): TickerStatistics

    /**
     * Get 24 hour price change statistics for all symbols.
     */
    val all24HrPriceStatistics: List<TickerStatistics>

    /**
     * Get Latest price for all symbols.
     */
    val allPrices: List<TickerPrice>

    /**
     * Get latest price for `symbol`.
     *
     * @param symbol ticker symbol (e.g. ETHBTC)
     */
    fun getPrice(symbol: String): TickerPrice

    /**
     * Get best price/qty on the order book for all symbols.
     */
    val bookTickers: List<BookTicker>

    // Account endpoints
    /**
     * Send in a new order.
     *
     * @param order the new order to submit.
     * @return a response containing details about the newly placed order.
     */
    fun newOrder(order: NewOrder): NewOrderResponse

    /**
     * Test new order creation and signature/recvWindow long. Creates and validates
     * a new order but does not send it into the matching engine.
     *
     * @param order the new TEST order to submit.
     */
    fun newOrderTest(order: NewOrder)

    /**
     * Check an order's status.
     *
     * @param orderStatusRequest order status request options/filters
     *
     * @return an order
     */
    fun getOrderStatus(orderStatusRequest: OrderStatusRequest): Order

    /**
     * Cancel an active order.
     *
     * @param cancelOrderRequest order status request parameters
     */
    fun cancelOrder(cancelOrderRequest: CancelOrderRequest): CancelOrderResponse

    /**
     * Get all open orders on a symbol.
     *
     * @param orderRequest order request parameters
     * @return a list of all account open orders on a symbol.
     */
    fun getOpenOrders(orderRequest: OrderRequest): List<Order>

    /**
     * Get all account orders; active, canceled, or filled.
     *
     * @param orderRequest order request parameters
     * @return a list of all account orders
     */
    fun getAllOrders(orderRequest: AllOrdersRequest): List<Order>

    /**
     * Get current account information.
     */
    fun getAccount(recvWindow: Long, timestamp: Long): Account

    /**
     * Get current account information using default parameters.
     */
    val account: Account

    /**
     * Get trades for a specific account and symbol.
     *
     * @param symbol symbol to get trades from
     * @param limit  default 500; max 1000
     * @param fromId TradeId to fetch from. Default gets most recent trades.
     * @return a list of trades
     */
    fun getMyTrades(
        symbol: String,
        limit: Int?,
        fromId: Long?,
        recvWindow: Long?,
        timestamp: Long?
    ): List<Trade>

    /**
     * Get trades for a specific account and symbol.
     *
     * @param symbol symbol to get trades from
     * @param limit  default 500; max 1000
     * @return a list of trades
     */
    fun getMyTrades(symbol: String, limit: Int): List<Trade>

    /**
     * Get trades for a specific account and symbol.
     *
     * @param symbol symbol to get trades from
     * @return a list of trades
     */
    fun getMyTrades(symbol: String): List<Trade>

    /**
     * Submit a withdraw request.
     * Enable Withdrawals option has to be active in the API settings.
     *
     * @param asset      asset symbol to withdraw
     * @param address    address to withdraw to
     * @param amount     amount to withdraw
     * @param name       description/alias of the address
     * @param addressTag Secondary address identifier for coins like XRP,XMR etc.
     */
    fun withdraw(asset: String, address: String, amount: String, name: String?, addressTag: String?): WithdrawResult

    /**
     * Fetch account deposit history.
     *
     * @return deposit history, containing a list of deposits
     */
    fun getDepositHistory(asset: String): Array<Deposit>

    /**
     * Fetch account withdraw history.
     *
     * @return withdraw history, containing a list of withdrawals
     */
    fun getWithdrawHistory(asset: String): Array<Withdraw>

    /**
     * Fetch deposit address.
     *
     * @return deposit address for a given asset.
     */
    fun getDepositAddress(asset: String): DepositAddress

    /**
     * Convert dust assets to BNB
     *
     * @param assets
     */
    fun convertDust(assets: List<String>): DustConversionInfo

    /**
     * Return market cap of symbol
     *
     * @param symbol
     * @return
     */
    fun getMarketCap(symbol: String): String?
}