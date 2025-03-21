package org.example.udemykmp.coins.domain.api

import org.example.udemykmp.coins.data.remote.dto.CoinDetailsResponseDto
import org.example.udemykmp.coins.data.remote.dto.CoinPriceHistoryDto
import org.example.udemykmp.coins.data.remote.dto.CoinsListDto
import org.example.udemykmp.core.domain.DataError
import org.example.udemykmp.core.domain.Result

interface CoinsRemoteDataSource {
    suspend fun getCoinsList(): Result<CoinsListDto, DataError.Remote>
    suspend fun getPriceHistory(): Result<CoinPriceHistoryDto, DataError.Remote>
    suspend fun getCoinDetail(): Result<CoinDetailsResponseDto, DataError.Remote>
}