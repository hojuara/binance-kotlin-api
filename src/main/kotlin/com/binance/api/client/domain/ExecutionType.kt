package com.binance.api.client.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Order execution type.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
enum class ExecutionType {
    NEW,
    CANCELED,
    REPLACED,
    REJECTED,
    TRADE,
    EXPIRED
}