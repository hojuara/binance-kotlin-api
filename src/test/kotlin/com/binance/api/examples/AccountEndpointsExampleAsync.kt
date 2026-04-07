package com.binance.api.examples

import com.binance.api.client.BinanceApiClientFactory

/**
 * Examples on how to get account information asynchronously.
 */
object AccountEndpointsExampleAsync {

    @JvmStatic
    fun main(args: Array<String>) {
        val factory = BinanceApiClientFactory.newInstance("YOUR_API_KEY", "YOUR_SECRET")
        val client = factory.newAsyncRestClient()

        // Obter saldos da conta (async)
        // No Kotlin, se o último parâmetro for uma função, a lambda fica fora dos parênteses.
        client.getAccount { response ->
            println("Saldo de ETH: ${response!!.getAssetBalance("ETH")}")
        }

        // Obter lista de trades (async)
        client.getMyTrades("NEOETH") { response ->
            println("Trades: $response")
        }

        // Obter histórico de saques (async)
        client.getWithdrawHistory("ETH") { response ->
            println("Histórico de saques: $response")
        }

        // Obter histórico de depósitos (async)
        client.getDepositHistory("ETH") { response ->
            println("Histórico de depósitos: $response")
        }

        // Realizar um saque (async)
        client.withdraw("ETH", "0x123", "0.1", null, null) {
            println("Solicitação de saque enviada.")
        }

        // Nota: Como as chamadas são assíncronas, em um script main real,
        // você precisaria de um mecanismo para impedir que a JVM feche antes da resposta.
        Thread.sleep(5000)
    }
}