package com.binance.api.examples

import com.binance.api.client.BinanceApiClientFactory
import com.binance.api.client.domain.account.AssetBalance
import com.binance.api.client.domain.event.UserDataUpdateEvent.UserDataUpdateEventType.ACCOUNT_UPDATE
import java.util.*

/**
 * Illustrates how to use the user data event stream to create a local cache for
 * the balance of an account.
 */
class AccountBalanceCacheExample(apiKey: String, secret: String) {

    private val clientFactory = BinanceApiClientFactory.newInstance(apiKey, secret)

    /**
     * Key is the symbol, and the value is the balance of that symbol on the
     * account.
     */
    val accountBalanceCache: MutableMap<String, AssetBalance> = TreeMap()

    /**
     * Listen key used to interact with the user data streaming API.
     */
    private val listenKey: String

    init {
        this.listenKey = initializeAssetBalanceCacheAndStreamSession()
        startAccountBalanceEventStreaming(listenKey)
    }

    /**
     * Initializes the asset balance cache by using the REST API and starts a new
     * user data streaming session.
     *
     * @return a listenKey that can be used with the user data streaming API.
     */
    private fun initializeAssetBalanceCacheAndStreamSession(): String {
        val client = clientFactory.newRestClient()
        val account = client.getAccount()

        // Popula o cache inicial usando o retorno da conta
        account.balances.forEach { assetBalance ->
            accountBalanceCache[assetBalance.asset!!] = assetBalance
        }

        throw UnsupportedOperationException("UserDataStream listenKey API was removed from Binance API")
    }

    /**
     * Begins streaming of user data events.
     */
    private fun startAccountBalanceEventStreaming(listenKey: String) {
        val client = clientFactory.newWebSocketClient()

        client.onUserDataUpdateEvent() { response ->
            if (response.eventType == ACCOUNT_UPDATE) {
                response.accountUpdateEvent!!.balances.forEach { assetBalance ->
                    accountBalanceCache[assetBalance.asset!!] = assetBalance
                }
                println("Cache atualizado: $accountBalanceCache")
            }
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            AccountBalanceCacheExample("YOUR_API_KEY", "YOUR_SECRET")
        }
    }
}