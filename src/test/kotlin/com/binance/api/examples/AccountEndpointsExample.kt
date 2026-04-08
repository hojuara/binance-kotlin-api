package com.binance.api.examples

import com.binance.api.client.BinanceApiClientFactory

/**
 * Examples on how to get account information.
 */
object AccountEndpointsExample {

    @JvmStatic
    fun main(args: Array<String>) {
        val factory = BinanceApiClientFactory.newInstance("YOUR_API_KEY", "YOUR_SECRET")
        val client = factory.newRestClient()

        // Obter saldos da conta (passando recvWindow e timestamp explicitamente)
        val account = client.getAccount()
        println("Todos os saldos: ${account.balances}")
        println("Saldo de ETH: ${account.getAssetBalance("ETH")}")

        // Obter lista de trades de um símbolo específico
        val myTrades = client.getMyTrades("NEOETH")
        println("Meus trades em NEOETH: $myTrades")

        // Histórico de saques e depósitos
        println("Histórico de saques de ETH: ${client.getWithdrawHistory("ETH")}")
        println("Histórico de depósitos de ETH: ${client.getDepositHistory("ETH")}")

        // Obter endereço de depósito
        println("Endereço de depósito de ETH: ${client.getDepositAddress("ETH")}")

        // Realizar um saque
        // No Kotlin, podemos usar argumentos nomeados se a interface permitir,
        // ou apenas passar os valores de forma clara.
        client.withdraw("ETH", "0x123", "0.1", null, null)
    }
}