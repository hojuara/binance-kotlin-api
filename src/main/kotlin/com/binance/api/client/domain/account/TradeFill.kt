package com.binance.api.client.domain.account

data class TradeFill(
    val tradeId: Long,
    val commissionAsset: String,
    val price: String,
    val qty: String,
    val commission: String
)
