package com.binance.api.client.domain.event

import java.util.UUID

data class SubscribeRequest(
    val method: SubscribeRequestMethod,
    val params: MutableMap<String, Any>,
    val id: String = UUID.randomUUID().toString()
)
