package com.binance.api.client.domain.account

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * A withdraw that was done to a Binance account.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Withdraw(
    /**
     * Amount withdrawn.
     */
    val amount: String,

    /**
     * Destination address.
     */
    val address: String,

    /**
     * Symbol.
     */
    val coin: String,

    val applyTime: String,

    val successTime: String,

    /**
     * Transaction id.
     */
    val txId: String,

    /**
     * Id.
     */
    val id: String,

    /**
     * (0:Email Sent,1:Cancelled 2:Awaiting Approval 3:Rejected 4:Processing
     * 5:Failure 6:Completed)
     */
    val status: Int
)