package org.example.udemykmp.core.navigation

import kotlinx.serialization.Serializable
import org.example.udemykmp.core.navigation.Destination.Trade.Type

@Serializable
sealed class Destination {
    @Serializable
    object Coins

    @Serializable
    object Portfolio

    @Serializable
    data class Trade(
        val coinId: String,
        val tradeType: Int,
    ) {
        enum class Type {
            BUY,
            SELL,
        }
    }
}

//region WORKAROUND
/**
 *
 * CMP navigation doesn't support enum type arguments.
 * Even if we implement a custom NavType, it will crash on iOS.
 */
fun encodeType(type: Type): Int {
    return when (type) {
        Type.BUY -> 1
        Type.SELL -> -1
    }
}

fun decodeType(destination: Int): Type {
    return when (destination) {
        1 -> Type.BUY
        -1 -> Type.SELL
        else -> throw IllegalArgumentException("Invalid destination: $destination")
    }
}
//endregion
