package com.binance.api.client.domain.account

import com.binance.api.client.constant.BinanceApiConstants
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.apache.commons.lang3.builder.ToStringBuilder

/**
 * Account information.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Account(
    var makerCommission: Int = 0,
    var takerCommission: Int = 0,
    var buyerCommission: Int = 0,
    var sellerCommission: Int = 0,
    var canTrade: Boolean = false,
    var canWithdraw: Boolean = false,
    var canDeposit: Boolean = false,
    var updateTime: Long = 0L,
    var balances: List<AssetBalance> = emptyList()
) {

    /**
     * Returns the asset balance for a given symbol.
     *
     * @param symbol asset symbol to obtain the balances from
     * @return an asset balance for the given symbol which can be 0 in case the
     * symbol has no balance in the account
     */
    fun getAssetBalance(symbol: String): AssetBalance {
        balances.firstOrNull { it.asset == symbol }?.let { return it }
        return AssetBalance(
            asset = symbol,
            free = "0",
            locked = "0"
        )
    }

    override fun toString(): String =
        ToStringBuilder(this, BinanceApiConstants.TO_STRING_BUILDER_STYLE)
            .append("makerCommission", makerCommission)
            .append("takerCommission", takerCommission)
            .append("buyerCommission", buyerCommission)
            .append("sellerCommission", sellerCommission)
            .append("canTrade", canTrade)
            .append("canWithdraw", canWithdraw)
            .append("canDeposit", canDeposit)
            .append("updateTime", updateTime)
            .append("balances", balances)
            .toString()
}

