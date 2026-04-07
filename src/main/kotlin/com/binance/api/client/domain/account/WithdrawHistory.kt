package com.binance.api.client.domain.account

import com.binance.api.client.constant.BinanceApiConstants
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.apache.commons.lang3.builder.ToStringBuilder

/**
 * History of account withdrawals.
 *
 * @see Withdraw
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class WithdrawHistory(
    var withdrawList: List<Withdraw>? = null,
    var isSuccess: Boolean = false
) {
    override fun toString(): String =
        ToStringBuilder(this, BinanceApiConstants.TO_STRING_BUILDER_STYLE)
            .append("withdrawList", withdrawList)
            .append("success", isSuccess)
            .toString()
}

