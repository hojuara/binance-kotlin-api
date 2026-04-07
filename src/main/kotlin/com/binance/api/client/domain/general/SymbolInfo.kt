package com.binance.api.client.domain.general

import com.binance.api.client.domain.OrderType
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Symbol information (base/quote).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class SymbolInfo(
    val symbol: String,
    val status: SymbolStatus,
    val baseAsset: String,
    val baseAssetPrecision: Int,
    val quoteAsset: String,
    val quotePrecision: Int,
    val orderTypes: List<OrderType>,
    @field:JsonProperty("icebergAllowed")
    @get:JsonProperty("icebergAllowed")
    val isIcebergAllowed: Boolean,
    val filters: List<SymbolFilter>,
    val permissions: List<String>
) {

    /**
     * @param filterType filter type to filter for.
     * @return symbol filter information for the provided filter type.
     */
    fun getSymbolFilter(filterType: FilterType): SymbolFilter? {
        return filters.find { it.filterType == filterType }
    }
}