package com.binance.api.client.domain.general

import com.binance.api.client.constant.BinanceApiConstants
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.apache.commons.lang3.builder.ToStringBuilder

/**
 * An asset Binance supports.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Asset(
    @JsonProperty("id")
    var id: String? = null,

    @JsonProperty("symbol")
    var assetCode: String? = null,

    @JsonProperty("name")
    var assetName: String? = null
) {
    override fun toString(): String =
        ToStringBuilder(this, BinanceApiConstants.TO_STRING_BUILDER_STYLE)
            .append("id", id)
            .append("symbol", assetCode)
            .append("name", assetName)
            .toString()
}

