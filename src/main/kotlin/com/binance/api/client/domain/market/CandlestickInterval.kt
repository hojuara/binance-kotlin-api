package com.binance.api.client.domain.market

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Kline/Candlestick intervals. m -> minutes; h -> hours; d -> days; w -> weeks;
 * M -> months
 */
@JsonIgnoreProperties(ignoreUnknown = true)
enum class CandlestickInterval(intervalId: String) {
    ONE_SECOND("1s"),
    ONE_MINUTE("1m"),
    THREE_MINUTES("3m"),
    FIVE_MINUTES("5m"),
    FIFTEEN_MINUTES("15m"),
    HALF_HOURLY("30m"),
    HOURLY("1h"),
    TWO_HOURLY("2h"),
    FOUR_HOURLY("4h"),
    SIX_HOURLY("6h"),
    EIGHT_HOURLY("8h"),
    TWELVE_HOURLY("12h"),
    DAILY("1d"),
    THREE_DAILY("3d"),
    WEEKLY("1w"),
    MONTHLY("1M");

    val intervalId: String?

    init {
        this.intervalId = intervalId
    }
}