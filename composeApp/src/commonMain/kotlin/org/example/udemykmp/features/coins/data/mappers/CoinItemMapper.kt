package org.example.udemykmp.features.coins.data.mappers

import org.example.udemykmp.features.coins.data.remote.dto.CoinItemDto
import org.example.udemykmp.features.coins.data.remote.dto.CoinPriceDto
import org.example.udemykmp.features.coins.domain.model.CoinModel
import org.example.udemykmp.features.coins.domain.model.CoinPriceHistory
import org.example.udemykmp.core.domain.coin.Coin

fun CoinItemDto.toCoinModel() = CoinModel(
    coin = Coin(
        id = uuid,
        name = name,
        symbol = symbol,
        iconUrl = iconUrl,
    ),
    price = price,
    change = change,
)

fun CoinPriceDto.toPriceModel() = CoinPriceHistory(
    price = price ?: 0.0,
    timestamp = timestamp
)