package com.binance.api.client.domain.general

import com.binance.api.client.exception.BinanceApiException
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Current exchange trading rules and symbol information.
 * https://github.com/binance-exchange/binance-official-api-docs/blob/master/rest-api.md
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class ExchangeInfo(
    val timezone: String,
    val serverTime: Long,
    val rateLimits: List<RateLimit>,
    val symbols: List<SymbolInfo>
) {

    /**
     * @param symbol the symbol to obtain information for (e.g. ETHBTC)
     * @return symbol exchange information
     */
    fun getSymbolInfo(symbol: String): SymbolInfo {
        return symbols.find { it.symbol == symbol }
            ?: throw BinanceApiException("Unable to obtain information for symbol $symbol")
    }
}