package org.example.udemykmp.features.coins.integration

import org.example.udemykmp.features.coins.data.remote.dto.CoinDetailsResponseDto
import org.example.udemykmp.features.coins.data.remote.dto.CoinPriceHistoryResponseDto
import org.example.udemykmp.features.coins.data.remote.dto.CoinsListResponseDto
import org.example.udemykmp.core.domain.DataError
import org.example.udemykmp.core.domain.Result

interface CoinsRemoteDataSource {
    suspend fun getCoinsList(): Result<CoinsListResponseDto, DataError.Remote>
    suspend fun getPriceHistory(coinId: String): Result<CoinPriceHistoryResponseDto, DataError.Remote>
    suspend fun getCoinDetailsOf(coinId: String): Result<CoinDetailsResponseDto, DataError.Remote>
}