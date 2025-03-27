package org.example.udemykmp.coins.data.mappers

import org.example.udemykmp.coins.data.remote.dto.CoinItemDto
import org.example.udemykmp.coins.data.remote.dto.CoinPriceDto
import org.example.udemykmp.coins.domain.model.CoinModel
import org.example.udemykmp.coins.domain.model.PriceModel
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

fun CoinPriceDto.toPriceModel() = PriceModel(
    price = price ?: 0.0,
    timestamp = timestamp
)