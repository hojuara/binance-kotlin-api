package com.binance.api.client.domain.account

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * A deposit address for a given asset.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class DepositAddress(
    val address: String,
    val coin: String,
    val tag: String,
    val url: String
)