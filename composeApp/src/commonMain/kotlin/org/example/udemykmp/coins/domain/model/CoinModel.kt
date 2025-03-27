package org.example.udemykmp.coins.domain.model

import org.example.udemykmp.core.domain.coin.Coin

data class CoinModel(
    val coin: Coin,
    val price: Double,
    val change: Double,
)
