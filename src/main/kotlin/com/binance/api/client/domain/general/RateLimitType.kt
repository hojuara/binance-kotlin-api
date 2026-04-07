package com.binance.api.client.domain.general

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Rate limiters.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
enum class RateLimitType {
    REQUEST_WEIGHT,
    ORDERS,
    RAW_REQUESTS
}