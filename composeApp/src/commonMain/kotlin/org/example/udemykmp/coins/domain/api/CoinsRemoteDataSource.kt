package org.example.udemykmp.coins.domain.api

import org.example.udemykmp.coins.data.remote.dto.CoinDetailsResponseDto
import org.example.udemykmp.coins.data.remote.dto.CoinPriceHistoryDto
import org.example.udemykmp.coins.data.remote.dto.CoinsListDto
import org.example.udemykmp.coins.data.remote.dto.CoinsListResponseDto
import org.example.udemykmp.core.domain.DataError
import org.example.udemykmp.core.domain.Result

interface CoinsRemoteDataSource {
    suspend fun getCoinsList(): Result<CoinsListResponseDto, DataError.Remote>
    suspend fun getPriceHistory(coinId: String): Result<CoinPriceHistoryDto, DataError.Remote>
    suspend fun getCoinDetailsOf(coinId: String): Result<CoinDetailsResponseDto, DataError.Remote>
}