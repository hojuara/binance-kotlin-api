package com.binance.api.client.domain.general

import com.binance.api.client.constant.BinanceApiConstants
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Rate limits.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class RateLimit(
    var rateLimitType: RateLimitType? = null,
    var interval: RateLimitInterval? = null,
    var limit: Int? = null
)