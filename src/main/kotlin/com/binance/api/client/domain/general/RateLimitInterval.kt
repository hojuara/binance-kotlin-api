package com.binance.api.client.domain.general

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Rate limit intervals.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
enum class RateLimitInterval {
    SECOND,
    MINUTE,
    DAY
}