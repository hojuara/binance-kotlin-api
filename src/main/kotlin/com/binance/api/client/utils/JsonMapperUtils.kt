package com.binance.api.client.utils

import tools.jackson.core.StreamWriteFeature
import tools.jackson.core.json.JsonWriteFeature
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.cfg.JsonNodeFeature
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.KotlinModule

object JsonMapperUtils {

    private var mapper: ObjectMapper? = null

    fun getInstance(): ObjectMapper {
        synchronized(this) {
            mapper = mapper ?: JsonMapper.builder()
                .addModule(KotlinModule.Builder().build())
                .enable(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN)
                .enable(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS)
                .enable(JsonNodeFeature.STRIP_TRAILING_BIGDECIMAL_ZEROES)
                .build()
        }
        return mapper!!
    }
}