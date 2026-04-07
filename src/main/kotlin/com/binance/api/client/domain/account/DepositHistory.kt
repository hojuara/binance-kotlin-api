package com.binance.api.client.domain.account

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * History of account deposits.
 *
 * @see Deposit
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class DepositHistory(
    @field:JsonProperty("depositList")
    var depositList: List<Deposit> = mutableListOf(),

    var isSuccess: Boolean = false,

    var msg: String? = null
)