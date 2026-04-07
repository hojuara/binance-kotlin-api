package com.binance.api.client.domain.general

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Filters define trading rules on a symbol or an exchange. Filters come in two
 * forms: symbol filters and exchange filters.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class SymbolFilter(
    var filterType: FilterType? = null,

    /**
     * PRICE_FILTER: Defines the minimum price/stopPrice allowed.
     */
    var minPrice: String? = null,

    /**
     * PRICE_FILTER: Defines the maximum price/stopPrice allowed.
     */
    var maxPrice: String? = null,

    /**
     * PRICE_FILTER: Defines the intervals that a price/stopPrice can be increased/decreased by.
     */
    var tickSize: String? = null,

    /**
     * LOT_SIZE: Defines the minimum quantity/icebergQty allowed.
     */
    var minQty: String? = null,

    /**
     * LOT_SIZE: Defines the maximum quantity/icebergQty allowed.
     */
    var maxQty: String? = null,

    /**
     * LOT_SIZE: Defines the intervals that a quantity/icebergQty can be increased/decreased by.
     */
    var stepSize: String? = null,

    /**
     * NOTIONAL: Defines the minimum and maximum notional value allowed for an order on a symbol.
     */
    var minNotional: String? = null,
    var maxNotional: String? = null,

    /**
     * MAX_NUM_ALGO_ORDERS: Defines the maximum number of "algo" orders an account is allowed to have
     * open on a symbol.
     */
    var maxNumAlgoOrders: String? = null,

    /**
     * Generic limit for various filters (MAX_NUM_ORDERS, MAX_ALGO_ORDERS, ICEBERG_PARTS).
     */
    var limit: String? = null,

    /**
     * TRAILING_DELTA: Defines the minimum and maximum value for the parameter trailingDelta.
     */
    var minTrailingAboveDelta: String? = null,
    var maxTrailingAboveDelta: String? = null,
    var minTrailingBelowDelta: String? = null,
    var maxTrailingBelowDelta: String? = null
)