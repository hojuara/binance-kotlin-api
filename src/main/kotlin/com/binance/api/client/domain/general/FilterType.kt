package com.binance.api.client.domain.general

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Filters define trading rules on a symbol or an exchange. Filters come in two
 * forms: symbol filters and exchange filters.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
enum class FilterType {
    // Symbol
    PRICE_FILTER,
    LOT_SIZE,
    NOTIONAL,
    MAX_NUM_ORDERS,
    MAX_ALGO_ORDERS,
    MAX_NUM_ALGO_ORDERS,
    ICEBERG_PARTS,
    PERCENT_PRICE,
    PERCENT_PRICE_BY_SIDE,
    MARKET_LOT_SIZE,
    MAX_NUM_ICEBERG_ORDERS,
    TRAILING_DELTA,
    MAX_NUM_ORDER_LISTS,
    MAX_NUM_ORDER_AMENDS,

    // Exchange
    EXCHANGE_MAX_NUM_ORDERS,
    EXCHANGE_MAX_ALGO_ORDERS,
    EXCHANGE_MAX_NUM_ICEBERG_ORDERS,
    EXCHANGE_MAX_NUM_ORDER_LISTS,

    // Unknown
    MAX_POSITION
}