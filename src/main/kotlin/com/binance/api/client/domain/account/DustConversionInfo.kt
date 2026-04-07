package com.binance.api.client.domain.account

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class DustConversionInfo(
    val totalServiceCharge: String,

    val totalTransfered: String,

    val transferResult: List<AssetDustResult>
)