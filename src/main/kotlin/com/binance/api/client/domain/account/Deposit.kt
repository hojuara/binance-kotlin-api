package com.binance.api.client.domain.account

import com.binance.api.client.constant.BinanceApiConstants
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.apache.commons.lang3.builder.ToStringBuilder

/**
 * A deposit that was done to a Binance account.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Deposit(
    var amount: String,
    var coin: String,
    val address: String,
    val addressTag: String,
    var insertTime: String,
    var txId: String,
    var status: Int
) {
    override fun toString(): String =
        ToStringBuilder(this, BinanceApiConstants.TO_STRING_BUILDER_STYLE)
            .append("amount", amount)
            .append("coin", coin)
            .append("insertTime", insertTime)
            .append("txId", txId)
            .append("status", status)
            .toString()
}

