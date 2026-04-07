package com.binance.api.client.domain.account

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class AssetDustResult(
    val amount: String,
    val fromAsset: String,
    val operateTime: Long,
    val serviceChargeAmount: String,
    val tranId: Long,
    val transferedAmount: String
)