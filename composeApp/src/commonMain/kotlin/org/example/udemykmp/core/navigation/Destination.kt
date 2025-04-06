package org.example.udemykmp.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Destination {
    @Serializable
    object Coins

    @Serializable
    object Portfolio

    @Serializable
    data class Trade(
        val coinId: String,
        val tradeType: Type,
    ) {
        enum class Type {
            BUY,
            SELL,
        }
    }
}