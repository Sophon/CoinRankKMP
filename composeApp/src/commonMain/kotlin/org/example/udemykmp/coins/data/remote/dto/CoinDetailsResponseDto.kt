package org.example.udemykmp.coins.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoinDetailsResponseDto(
    val data: CoinResponseDto,
)

@Serializable
data class CoinResponseDto(
    val coin: CoinItemDto,
)