package com.binance.api.client.impl

import com.binance.api.client.BinanceApiCallback
import com.binance.api.client.BinanceApiWebSocketClient
import com.binance.api.client.constant.BinanceApiConstants
import com.binance.api.client.domain.event.*
import com.binance.api.client.domain.market.CandlestickInterval
import com.binance.api.client.exception.BinanceApiException
import com.binance.api.client.security.HmacSHA256Signer
import com.binance.api.client.utils.JsonMapperUtils
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.handler.AbstractWebSocketHandler
import tools.jackson.core.type.TypeReference
import java.io.Closeable
import java.net.URI
import java.util.*


/**
 * Binance API WebSocket client implementation using Spring's [StandardWebSocketClient].
 */
class BinanceApiWebSocketClientImpl(
    private val apiKey: String?,
    private val secret: String?
) : BinanceApiWebSocketClient {

    private val client = StandardWebSocketClient()
    private val mapper = JsonMapperUtils.getInstance()

    override fun onDepthEvent(symbols: String, callback: BinanceApiCallback<DepthEvent>): Closeable {
        val channel = symbols.split(",")
            .map { it.trim() }
            .joinToString("/") { "${it}@depth@100ms" }
        return createNewWebSocket(channel, callback, object : TypeReference<DepthEvent>() {})
    }

    override fun onCandlestickEvent(
        symbols: String,
        interval: CandlestickInterval,
        callback: BinanceApiCallback<CandlestickEvent>
    ): Closeable {
        val channel = symbols.split(",")
            .map { it.trim() }
            .joinToString("/") { "${it}@kline_${interval.intervalId}" }
        return createNewWebSocket(channel, callback, object : TypeReference<CandlestickEvent>() {})
    }

    override fun onAggTradeEvent(
        symbols: String,
        callback: BinanceApiCallback<AggTradeEvent>
    ): Closeable {
        val channel = symbols.split(",")
            .map { it.trim() }
            .joinToString("/") { "${it}@aggTrade" }
        return createNewWebSocket(channel, callback, object : TypeReference<AggTradeEvent>() {})
    }

    override fun onUserDataUpdateEvent(
        callback: BinanceApiCallback<UserDataUpdateEvent>
    ): Closeable {
        val currentTime = System.currentTimeMillis()
        val signature = generateSignature(currentTime)
        return createNewWebSocket("", callback, object : TypeReference<UserDataUpdateEvent>() {}, {
            val payload = """
                {
                    "id":"${UUID.randomUUID()}",
                    "method":"userDataStream.subscribe.signature",
                    "params": {
                        "apiKey": "$apiKey",
                        "timestamp": $currentTime,
                        "signature": "$signature"                    
                    }
                }
            """.trimIndent()
            it!!.sendMessage(TextMessage(payload))
        })
    }

    @Throws(Exception::class)
    fun generateSignature(currentTime: Long): String {
        if (apiKey.isNullOrBlank() || secret.isNullOrBlank()) {
            throw BinanceApiException("API key and Secret is required for subscribe on User Data Stream")
        }
        val params = listOf(
            "apiKey" to apiKey,
            "timestamp" to currentTime
        )
        val payload = params.map { "${it.first}=${it.second}" }
            .joinToString("&")
        return HmacSHA256Signer.sign(payload, secret)
    }

    override fun onAllMarketTickersEvent(
        callback: BinanceApiCallback<List<AllMarketTickersEvent>>
    ): Closeable {
        val channel = "!ticker_1d@arr"
        return createNewWebSocket(channel, callback, object : TypeReference<List<AllMarketTickersEvent>>() {})
    }

    override fun onPartialDepthEvent(
        symbol: String,
        limit: Int,
        callback: BinanceApiCallback<BookDepthEvent>
    ): Closeable {
        val channel = "${symbol}@depth5@100ms"
        return createNewWebSocket(channel, callback, object : TypeReference<BookDepthEvent>() {})
    }

    override fun onBookTickerEvent(
        symbols: String,
        callback: BinanceApiCallback<BookTickerEvent>
    ): Closeable {
        val channel = symbols.split(",")
            .map { it.trim() }
            .joinToString("/") { "${it}@bookTicker" }
        return createNewWebSocket(channel, callback, object : TypeReference<BookTickerEvent>() {})
    }

    private fun <T> createNewWebSocket(
        channel: String,
        callback: BinanceApiCallback<T>,
        typeReference: TypeReference<T>,
        afterConnectionEstablishedCallback: (session: WebSocketSession?) -> Unit = {}
    ): Closeable {
        val streamingUrl = "${BinanceApiConstants.WS_API_BASE_URL}/$channel"
        val uri = URI.create(streamingUrl)

        val handler = object : AbstractWebSocketHandler() {

            @Throws(Exception::class)
            override fun afterConnectionEstablished(session: WebSocketSession) {
                afterConnectionEstablishedCallback.invoke(session)
            }

            @Throws(Exception::class)
            override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
                try {
                    val event: T = mapper.readValue(message.payload, typeReference)
                    callback.onResponse(event)
                } catch (ex: Exception) {
                    throw BinanceApiException(ex)
                }
            }

            override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
                callback.onFailure(exception)
            }
        }

        val future = client.execute(handler, uri.toString())
        val session = future.get()

        return Closeable {
            if (session.isOpen) {
                session.close()
            }
        }
    }
}

