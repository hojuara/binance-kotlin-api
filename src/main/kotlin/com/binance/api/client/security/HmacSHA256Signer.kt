package com.binance.api.client.security

import org.apache.commons.codec.binary.Hex
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Utility class to sign messages using HMAC-SHA256.
 */
object HmacSHA256Signer {

    /**
     * Sign the given message using the given secret.
     *
     * @param message message to sign
     * @param secret  secret key
     * @return a signed message
     */
    @JvmStatic
    fun sign(message: String, secret: String): String {
        return try {
            val hmacSha256 = Mac.getInstance("HmacSHA256")
            val secretKeySpec = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
            hmacSha256.init(secretKeySpec)

            val hashBytes = hmacSha256.doFinal(message.toByteArray())
            String(Hex.encodeHex(hashBytes))
        } catch (e: Exception) {
            throw RuntimeException("Unable to sign message.", e)
        }
    }
}