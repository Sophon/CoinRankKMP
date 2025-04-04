package org.example.udemykmp.features.coins.integration.model

import org.example.udemykmp.core.domain.coin.Coin

data class CoinModel(
    val coin: Coin,
    val price: Double,
    val change: Double,
)
