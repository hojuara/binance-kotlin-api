package com.binance.api.client.impl

import com.binance.api.client.BinanceApiError
import com.binance.api.client.BinanceApiRestClient
import com.binance.api.client.constant.BinanceApiConstants
import com.binance.api.client.domain.account.*
import com.binance.api.client.domain.account.request.*
import com.binance.api.client.domain.general.Asset
import com.binance.api.client.domain.general.CirculationSupplyInfo
import com.binance.api.client.domain.general.ExchangeInfo
import com.binance.api.client.domain.market.*
import com.binance.api.client.exception.BinanceApiException
import com.binance.api.client.security.HmacSHA256Signer
import com.binance.api.client.utils.JsonMapperUtils
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClientResponseException
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.JsonNode
import java.math.BigDecimal
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicInteger

/**
 * Spring RestClient-based implementation of [BinanceApiRestClient].
 */
class BinanceApiRestClientImpl(
    private val apiKey: String?,
    private val secret: String?
) : BinanceApiRestClient {

    // Cache for market cap calculation (kept similar to the original implementation)
    private var cachedExchangeProductInfo: List<CirculationSupplyInfo>? = null
    private var lastCachedExchangeUpdate: LocalDate? = null

    companion object {
        internal const val COINGECKO_COINLIST_API: String =
            "https://api.coingecko.com/api/v3/coins/list"

        internal const val BINANCE_EXCHANGE_GETPRODUCTS_API: String =
            "https://www.binance.com/bapi/asset/v2/friendly/asset-service/product/get-product-static?includeEtf=true"
    }

    private val objectMapper = JsonMapperUtils.getInstance()

    private val restClients: List<RestClient> = BinanceApiConstants.API_BASE_URL_CLUSTER
        .map { baseUrl ->
            RestClient.builder()
                .baseUrl(baseUrl)
                .build()
        }

    private val nextIndex = AtomicInteger(0)

    private fun nextClient(): RestClient =
        restClients[nextIndex.getAndUpdate { (it + 1) % restClients.size }]

    private fun <T : Any> execute(
        method: HttpMethod,
        path: String,
        queryParams: List<Pair<String, Any?>>,
        responseType: Class<T>,
        signed: Boolean = false
    ): T {
        val client = nextClient()
        val fullPath = if (signed) buildSignedPath(path, queryParams) else buildPath(path, queryParams)
        try {
            val requestSpec = when (method) {
                HttpMethod.GET -> client.get().uri(fullPath)
                HttpMethod.POST -> client.post().uri(fullPath)
                HttpMethod.PUT -> client.put().uri(fullPath)
                HttpMethod.DELETE -> client.delete().uri(fullPath)
                else -> throw IllegalArgumentException("HTTP method $method not supported")
            }.apply {
                if (!apiKey.isNullOrBlank()) {
                    header(BinanceApiConstants.API_KEY_HEADER, apiKey)
                }
            }
            val body = requestSpec.retrieve().body(responseType)
            if (responseType == Void::class.java || responseType == Nothing::class.java) {
                @Suppress("UNCHECKED_CAST")
                return Any() as T
            } else if (body == null) {
                throw BinanceApiException("Empty response body for $method $fullPath")
            }
            return body
        } catch (ex: RestClientResponseException) {
            throw toBinanceException(ex)
        } catch (ex: Exception) {
            throw BinanceApiException(ex)
        }
    }

    private fun buildPath(path: String, queryParams: List<Pair<String, Any?>>): String {
        if (queryParams.isEmpty()) return path
        val query = queryParams
            .filter { it.second != null }
            .joinToString("&") { (key, value) ->
                "$key=${URLEncoder.encode(value.toString(), StandardCharsets.UTF_8)}"
            }
        return if (query.isEmpty()) path else "$path?$query"
    }

    private fun buildSignedPath(path: String?, queryParams: List<Pair<String, Any?>>): String {
        if (secret.isNullOrBlank()) {
            throw BinanceApiException("API secret is required for signed endpoints")
        }
        val filtered = queryParams.filter { it.second != null }
        val queryWithoutSig = filtered.joinToString("&") { (key, value) ->
            "$key=${URLEncoder.encode(value.toString(), StandardCharsets.UTF_8)}"
        }
        val signature = HmacSHA256Signer.sign(queryWithoutSig, secret)
        val fullQuery = queryWithoutSig.takeIf { it.isNotEmpty() }?.let { "?$queryWithoutSig&signature=$signature" } ?: ""
        return "$path$fullQuery"
    }

    private fun toBinanceException(ex: RestClientResponseException): BinanceApiException {
        val body = ex.responseBodyAsString
        return try {
            val error = objectMapper.readValue(body, BinanceApiError::class.java)
            BinanceApiException(error)
        } catch (_: Exception) {
            BinanceApiException(ex)
        }
    }

    // General endpoints

    override fun ping() {
        // Binance ping endpoint returns HTTP 200 with empty body
        execute(HttpMethod.GET, "/api/v3/ping", emptyList(), Void::class.java)
    }

    override val serverTime: Long
        get() {
            val serverTime = execute(HttpMethod.GET, "/api/v3/time", emptyList(), com.binance.api.client.domain.general.ServerTime::class.java)
            return serverTime.serverTime
        }

    override val exchangeInfo: ExchangeInfo
        get() = execute(HttpMethod.GET, "/api/v3/exchangeInfo", emptyList(), ExchangeInfo::class.java)

    override val allAssets: List<Asset>
        get() {
            // This endpoint uses the public CoinGecko API directly
            val client = RestClient.create()
            return try {
                val body = client.get()
                    .uri(COINGECKO_COINLIST_API)
                    .retrieve()
                    .body(Array<Asset>::class.java)
                body?.toList() ?: emptyList()
            } catch (ex: RestClientResponseException) {
                throw toBinanceException(ex)
            } catch (ex: Exception) {
                throw BinanceApiException(ex)
            }
        }

    // Market Data endpoints

    override fun getOrderBook(symbol: String, limit: Int): OrderBook =
        execute(
            HttpMethod.GET,
            "/api/v3/depth",
            listOf("symbol" to symbol, "limit" to limit),
            OrderBook::class.java
        )

    override fun getTrades(symbol: String, limit: Int): List<TradeHistoryItem> =
        execute(
            HttpMethod.GET,
            "/api/v1/trades",
            listOf("symbol" to symbol, "limit" to limit),
            Array<TradeHistoryItem>::class.java
        ).toList()

    override fun getHistoricalTrades(symbol: String, limit: Int, fromId: Long): List<TradeHistoryItem> =
        execute(
            HttpMethod.GET,
            "/api/v3/historicalTrades",
            listOf("symbol" to symbol, "limit" to limit, "fromId" to fromId),
            Array<TradeHistoryItem>::class.java
        ).toList()

    override fun getAggTrades(
        symbol: String,
        fromId: String?,
        limit: Int?,
        startTime: Long?,
        endTime: Long?
    ): List<AggTrade> =
        execute(
            HttpMethod.GET,
            "/api/v3/aggTrades",
            listOf(
                "symbol" to symbol,
                "fromId" to fromId,
                "limit" to limit,
                "startTime" to startTime,
                "endTime" to endTime
            ),
            Array<AggTrade>::class.java
        ).toList()

    override fun getAggTrades(symbol: String): List<AggTrade> =
        getAggTrades(symbol, null, null, null, null)

    override fun getCandlestickBars(
        symbol: String,
        interval: CandlestickInterval,
        limit: Int?,
        startTime: Long?,
        endTime: Long?
    ): List<Candlestick> =
        execute(
            HttpMethod.GET,
            "/api/v3/klines",
            listOf(
                "symbol" to symbol,
                "interval" to interval.intervalId,
                "limit" to limit,
                "startTime" to startTime,
                "endTime" to endTime
            ),
            Array<Candlestick>::class.java
        ).toList()

    override fun getAvgPrice(symbol: String): AvgPrice =
        execute(
            HttpMethod.GET,
            "/api/v3/avgPrice",
            listOf("symbol" to symbol),
            AvgPrice::class.java
        )

    override fun getCandlestickBars(symbol: String, interval: CandlestickInterval): List<Candlestick> =
        getCandlestickBars(symbol, interval, null, null, null)

    override fun get24HrPriceStatistics(symbol: String): TickerStatistics =
        execute(
            HttpMethod.GET,
            "/api/v3/ticker/24hr",
            listOf("symbol" to symbol),
            TickerStatistics::class.java
        )

    override val all24HrPriceStatistics: List<TickerStatistics>
        get() = execute(
            HttpMethod.GET,
            "/api/v3/ticker/24hr",
            emptyList(),
            Array<TickerStatistics>::class.java
        ).toList()

    override val allPrices: List<TickerPrice>
        get() = execute(
            HttpMethod.GET,
            "/api/v3/ticker/price",
            emptyList(),
            Array<TickerPrice>::class.java
        ).toList()

    override fun getPrice(symbol: String): TickerPrice =
        execute(
            HttpMethod.GET,
            "/api/v3/ticker/price",
            listOf("symbol" to symbol),
            TickerPrice::class.java
        )

    override val bookTickers: List<BookTicker>
        get() = execute(
            HttpMethod.GET,
            "/api/v3/ticker/bookTicker",
            emptyList(),
            Array<BookTicker>::class.java
        ).toList()

    // Account endpoints

    override fun newOrder(order: NewOrder): NewOrderResponse =
        execute(
            HttpMethod.POST,
            "/api/v3/order",
            makeNewOrderParams(order),
            NewOrderResponse::class.java,
            signed = true
        )

    override fun newOrderTest(order: NewOrder) {
        execute(
            HttpMethod.POST,
            "/api/v3/order/test",
            makeNewOrderParams(order),
            Void::class.java,
            signed = true
        )
    }

    private fun makeNewOrderParams(order: NewOrder) =
        listOf(
            "symbol" to order.symbol,
            "side" to order.side,
            "type" to order.type,
            "timeInForce" to order.timeInForce,
            "quantity" to order.quantity,
            "quoteOrderQty" to order.quoteOrderQty,
            "price" to order.price,
            "newClientOrderId" to order.newClientOrderId,
            "stopPrice" to order.stopPrice,
            "icebergQty" to order.icebergQty,
            "newOrderRespType" to order.newOrderRespType,
            "recvWindow" to order.recvWindow,
            "timestamp" to order.timestamp
        )

    override fun getOrderStatus(orderStatusRequest: OrderStatusRequest): Order =
        execute(
            HttpMethod.GET,
            "/api/v3/order",
            listOf(
                "symbol" to orderStatusRequest.symbol,
                "orderId" to orderStatusRequest.orderId,
                "origClientOrderId" to orderStatusRequest.origClientOrderId,
                "recvWindow" to orderStatusRequest.recvWindow,
                "timestamp" to orderStatusRequest.timestamp
            ),
            Order::class.java,
            signed = true
        )

    override fun cancelOrder(cancelOrderRequest: CancelOrderRequest): CancelOrderResponse =
        execute(
            HttpMethod.DELETE,
            "/api/v3/order",
            listOf(
                "symbol" to cancelOrderRequest.symbol,
                "orderId" to cancelOrderRequest.orderId,
                "origClientOrderId" to cancelOrderRequest.origClientOrderId,
                "newClientOrderId" to cancelOrderRequest.newClientOrderId,
                "recvWindow" to cancelOrderRequest.recvWindow,
                "timestamp" to cancelOrderRequest.timestamp
            ),
            CancelOrderResponse::class.java,
            signed = true
        )

    override fun getOpenOrders(orderRequest: OrderRequest): List<Order> =
        execute(
            HttpMethod.GET,
            "/api/v3/openOrders",
            listOf(
                "symbol" to orderRequest.symbol,
                "recvWindow" to orderRequest.recvWindow,
                "timestamp" to orderRequest.timestamp
            ),
            Array<Order>::class.java,
            signed = true
        ).toList()

    override fun getAllOrders(orderRequest: AllOrdersRequest): List<Order> =
        execute(
            HttpMethod.GET,
            "/api/v3/allOrders",
            listOf(
                "symbol" to orderRequest.symbol,
                "orderId" to orderRequest.orderId,
                "limit" to orderRequest.limit,
                "recvWindow" to orderRequest.recvWindow,
                "timestamp" to orderRequest.timestamp
            ),
            Array<Order>::class.java,
            signed = true
        ).toList()

    override fun getAccount(recvWindow: Long, timestamp: Long): Account =
        execute(
            HttpMethod.GET,
            "/api/v3/account",
            listOf(
                "recvWindow" to recvWindow,
                "timestamp" to timestamp
            ),
            Account::class.java,
            signed = true
        )

    override val account: Account
        get() = getAccount(BinanceApiConstants.DEFAULT_RECEIVING_WINDOW, System.currentTimeMillis())

    override fun getMyTrades(
        symbol: String,
        limit: Int?,
        fromId: Long?,
        recvWindow: Long?,
        timestamp: Long?
    ): List<Trade> =
        execute(
            HttpMethod.GET,
            "/api/v3/myTrades",
            listOf(
                "symbol" to symbol,
                "limit" to limit,
                "fromId" to fromId,
                "recvWindow" to recvWindow,
                "timestamp" to timestamp
            ),
            Array<Trade>::class.java,
            signed = true
        ).toList()

    override fun getMyTrades(symbol: String, limit: Int): List<Trade> =
        getMyTrades(
            symbol,
            limit,
            null,
            BinanceApiConstants.DEFAULT_RECEIVING_WINDOW,
            System.currentTimeMillis()
        )

    override fun getMyTrades(symbol: String): List<Trade> =
        getMyTrades(
            symbol,
            null,
            null,
            BinanceApiConstants.DEFAULT_RECEIVING_WINDOW,
            System.currentTimeMillis()
        )

    override fun withdraw(
        asset: String,
        address: String,
        amount: String,
        name: String?,
        addressTag: String?
    ): WithdrawResult =
        execute(
            HttpMethod.POST,
            "/sapi/v1/capital/withdraw/apply",
            listOf(
                "coin" to asset,
                "address" to address,
                "amount" to amount,
                "name" to name,
                "addressTag" to addressTag,
                "recvWindow" to BinanceApiConstants.DEFAULT_RECEIVING_WINDOW,
                "timestamp" to System.currentTimeMillis()
            ),
            WithdrawResult::class.java,
            signed = true
        )

    override fun getDepositHistory(asset: String): Array<Deposit> =
        execute(
            HttpMethod.GET,
            "/sapi/v1/capital/deposit/hisrec",
            listOf(
                "coin" to asset,
                "recvWindow" to BinanceApiConstants.DEFAULT_RECEIVING_WINDOW,
                "timestamp" to System.currentTimeMillis()
            ),
            Array<Deposit>::class.java,
            signed = true
        )

    override fun getWithdrawHistory(asset: String): Array<Withdraw> =
        execute(
            HttpMethod.GET,
            "/sapi/v1/capital/withdraw/history",
            listOf(
                "coin" to asset,
                "recvWindow" to BinanceApiConstants.DEFAULT_RECEIVING_WINDOW,
                "timestamp" to System.currentTimeMillis()
            ),
            Array<Withdraw>::class.java,
            signed = true
        )

    override fun getDepositAddress(asset: String): DepositAddress =
        execute(
            HttpMethod.GET,
            "/sapi/v1/capital/deposit/address",
            listOf(
                "coin" to asset,
                "recvWindow" to BinanceApiConstants.DEFAULT_RECEIVING_WINDOW,
                "timestamp" to System.currentTimeMillis()
            ),
            DepositAddress::class.java,
            signed = true
        )

    override fun convertDust(assets: List<String>): DustConversionInfo {
        val params = mutableListOf<Pair<String, Any>>()
        // Repeat the 'asset' query parameter for each asset, as the original Retrofit implementation did
        assets.forEach { asset ->
            params += "asset" to asset
        }
        params += "timestamp" to serverTime
        return execute(
            HttpMethod.POST,
            "/sapi/v1/asset/dust",
            params,
            DustConversionInfo::class.java,
            signed = true
        )
    }

    override fun getMarketCap(symbol: String): String? {
        val today = LocalDate.now()
        if (cachedExchangeProductInfo == null ||
            lastCachedExchangeUpdate == null ||
            today > lastCachedExchangeUpdate
        ) {
            cachedExchangeProductInfo = getExchangesProductInfo()
            lastCachedExchangeUpdate = today
        }

        val info = cachedExchangeProductInfo?.firstOrNull { it.symbol == symbol }
        return info?.let {
            val price = getPrice(it.symbol).let { t -> BigDecimal(t.price) }
            val circulationSupply = BigDecimal.valueOf(it.circulationSupply)
            return circulationSupply.multiply(price).toPlainString()
        }
    }

    private fun getExchangesProductInfo(): List<CirculationSupplyInfo>? {
        val client = RestClient.create()
        return try {
            val json = client.get()
                .uri(BINANCE_EXCHANGE_GETPRODUCTS_API)
                .retrieve()
                .body(JsonNode::class.java)
            val dataNode = json?.path("data")
            dataNode?.let {
                val mapper = JsonMapperUtils.getInstance()
                mapper.convertValue(
                    dataNode,
                    object : TypeReference<List<CirculationSupplyInfo>>() {}
                )
            }
        } catch (ex: RestClientResponseException) {
            throw toBinanceException(ex)
        } catch (ex: Exception) {
            throw BinanceApiException(ex)
        }
    }
}

