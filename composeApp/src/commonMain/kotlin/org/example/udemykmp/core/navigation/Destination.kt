package org.example.udemykmp.core.navigation

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
sealed class Destination {
    @Serializable
    object Coins

    @Serializable
    object Portfolio

    @Serializable
    data class Trade(
        val coinId: String,
        val tradeType: Type = Type.BUY,
    ) {
        enum class Type {
            BUY,
            SELL,
        }
    }
}

class TradeTypeNavType : NavType<Destination.Trade.Type>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Destination.Trade.Type? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): Destination.Trade.Type {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: Destination.Trade.Type) {
        return Json.encodeToString(value).let { bundle.putString(key, it) }
    }
}