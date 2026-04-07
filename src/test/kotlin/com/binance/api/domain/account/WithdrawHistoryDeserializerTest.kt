package com.binance.api.domain.account

import com.binance.api.client.domain.account.Withdraw
import com.binance.api.client.utils.JsonMapperUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.IOException

/**
 * Test deserialization of a withdraw/deposit history.
 */
class WithdrawHistoryDeserializerTest {

    @Test
    fun `test withdraw history deserialization`() {
        val withdrawHistoryJson = """
            [
                {
                    "amount": 0.1,
                    "address": "0x456",
                    "successTime": "2017-10-13 21:20:09",
                    "txId": "0x123",
                    "id": "1",
                    "coin": "ETH",
                    "applyTime": "2017-10-13 20:59:38",
                    "userId": "1",
                    "status": 6
                }
            ]
        """.trimIndent()

        val mapper = JsonMapperUtils.getInstance()
        try {
            val withdrawList = mapper.readValue(withdrawHistoryJson, Array<Withdraw>::class.java)

            assertEquals(1, withdrawList?.size, "O tamanho da lista de saques deve ser 1")

            withdrawList?.get(0)?.let { withdraw ->
                assertEquals("0.1", withdraw.amount)
                assertEquals("0x456", withdraw.address)
                assertEquals("ETH", withdraw.coin)
                assertEquals("2017-10-13 20:59:38", withdraw.applyTime)
                assertEquals("2017-10-13 21:20:09", withdraw.successTime)
                assertEquals("0x123", withdraw.txId)
                assertEquals("1", withdraw.id)
            } ?: fail("O primeiro item da lista de saques não deveria ser nulo")

        } catch (e: IOException) {
            fail("Erro na desserialização do JSON: ${e.message}")
        }
    }
}