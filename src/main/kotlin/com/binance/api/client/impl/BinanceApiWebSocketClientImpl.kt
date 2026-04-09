package com.binance.api.client.impl

import com.binance.api.client.BinanceApiCallback
import com.binance.api.client.BinanceApiWebSocketClient
import com.binance.api.client.constant.BinanceApiConstants
import com.binance.api.client.domain.event.*
import com.binance.api.client.domain.event.SubscribeRequestMethod.*
import com.binance.api.client.domain.market.CandlestickInterval
import com.binance.api.client.exception.BinanceApiException
import com.binance.api.client.security.HmacSHA256Signer
import com.binance.api.client.utils.JsonMapperUtils
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.handler.AbstractWebSocketHandler
import tools.jackson.core.type.TypeReference
import tools.jackson.module.kotlin.jacksonTypeRef
import java.io.Closeable
import java.net.URI


/**
 * Binance API WebSocket client implementation using Spring's [StandardWebSocketClient].
 */
class BinanceApiWebSocketClientImpl(
    private val apiKey: String?,
    private val secret: String?
) : BinanceApiWebSocketClient {

    private val client = StandardWebSocketClient()
    private val mapper = JsonMapperUtils.getInstance()

    override fun onDepthEvent(symbol: String, callback: BinanceApiCallback<DepthEvent>): Closeable {
        val request = SubscribeRequest(
            DEPTH, mutableMapOf(
                "symbol" to symbol,
                "limit" to 5
            )
        )
        return createNewWebSocket(request, callback, jacksonTypeRef<DepthEvent>())
    }

    override fun onCandlestickEvent(
        symbol: String,
        interval: CandlestickInterval,
        callback: BinanceApiCallback<Array<CandlestickEvent>>
    ): Closeable {
        val request = SubscribeRequest(
            CANDLESTICK, mutableMapOf(
                "symbol" to symbol,
                "interval" to interval.intervalId!!
            )
        )
        return createNewWebSocket(request, callback, jacksonTypeRef<Array<CandlestickEvent>>())
    }

    override fun onAggTradeEvent(
        symbol: String,
        callback: BinanceApiCallback<Array<AggTradeEvent>>
    ): Closeable {
        val request = SubscribeRequest(
            AGGREGATE_TRADES, mutableMapOf(
                "symbol" to symbol
            )
        )
        return createNewWebSocket(request, callback, jacksonTypeRef<Array<AggTradeEvent>>())
    }

    override fun onUserDataUpdateEvent(
        callback: BinanceApiCallback<UserDataUpdateEvent>
    ): Closeable {
        if (apiKey.isNullOrBlank() || secret.isNullOrBlank()) {
            throw BinanceApiException("API key is required for subscribe on User Data Stream")
        }
        val currentTime = System.currentTimeMillis()
        val params: MutableMap<String, Any> = mutableMapOf(
            "apiKey" to apiKey,
            "timestamp" to currentTime
        )
        val signature = generateSignature(params)
        params["signature"] = signature

        val request = SubscribeRequest(USER_DATA, params)
        return createNewWebSocket(request, callback, jacksonTypeRef<UserDataUpdateEvent>())
    }

    @Throws(Exception::class)
    fun generateSignature(params: Map<String, Any>): String {
        if (secret.isNullOrBlank()) {
            throw BinanceApiException("Secret is required for subscribe on User Data Stream")
        }
        val payload = params.map { (key, value) -> "${key}=${value}" }
            .joinToString("&")
        return HmacSHA256Signer.sign(payload, secret)
    }

    override fun onBookTickerEvent(
        symbols: Array<String>,
        callback: BinanceApiCallback<Array<BookTickerEvent>>
    ): Closeable {
        val request = SubscribeRequest(
            ORDER_BOOK_TICKER, mutableMapOf(
                "symbols" to symbols
            )
        )
        return createNewWebSocket(request, callback, jacksonTypeRef<Array<BookTickerEvent>>())
    }

    private fun <T> createNewWebSocket(
        request: SubscribeRequest,
        callback: BinanceApiCallback<T>,
        typeReference: TypeReference<T>
    ): Closeable {
        val uri = URI.create(BinanceApiConstants.WS_API_BASE_URL)
        val handler = object : AbstractWebSocketHandler() {

            @Throws(Exception::class)
            override fun afterConnectionEstablished(session: WebSocketSession) {
                val payload = mapper.writeValueAsString(request)
                session.sendMessage(TextMessage(payload))
            }

            @Throws(Exception::class)
            override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
                try {
                    val response: WebSocketResponse? = mapper.readValue(message.payload, WebSocketResponse::class.java)
                    // If the response is a UserDataUpdateEvent, the status will not be present and the event response will always be in the event attribute.
                    if (response != null && response.status == 200 || (response?.status == null && response?.event != null)) {
                        val resultNode = response.result ?: response.event
                        val event: T? = mapper.readValue(resultNode.toString(), typeReference)
                        event?.run { callback.onResponse(event) }
                    }
                } catch (ex: Exception) {
                    val exception = ex.takeIf { it is BinanceApiException } ?: BinanceApiException(ex)
                    handleTransportError(session, exception)
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

