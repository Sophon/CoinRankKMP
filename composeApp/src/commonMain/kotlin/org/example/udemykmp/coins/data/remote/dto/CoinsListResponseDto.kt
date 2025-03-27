package org.example.udemykmp.coins.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoinsListResponseDto(
    val data: CoinsListDto,
)

@Serializable
data class CoinsListDto(
    val coins: List<CoinItemDto>,
)

@Serializable
data class CoinItemDto(
    val uuid: String,
    val symbol: String,
    val name: String,
    val iconUrl: String,
    val price: Double,
    val rank: Int,
    val change: Double,
)