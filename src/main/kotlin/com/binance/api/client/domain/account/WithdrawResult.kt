package com.binance.api.client.domain.account

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * A withdraw result that was done to a Binance account.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class WithdrawResult(
    /**
     * Withdraw id.
     */
    val id: String
)