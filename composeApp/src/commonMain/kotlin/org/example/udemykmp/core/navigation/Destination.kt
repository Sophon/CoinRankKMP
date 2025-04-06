package org.example.udemykmp.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Destination {
    @Serializable
    object Coins

    @Serializable
    object Portfolio

    @Serializable
    object Trade
}