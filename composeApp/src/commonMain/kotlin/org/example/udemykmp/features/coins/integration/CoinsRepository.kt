package org.example.udemykmp.features.coins.integration

import org.example.udemykmp.core.domain.DataError
import org.example.udemykmp.core.domain.Result
import org.example.udemykmp.features.coins.data.remote.dto.CoinsListResponseDto
import org.example.udemykmp.features.coins.integration.model.CoinModel
import org.example.udemykmp.features.coins.integration.model.CoinPriceHistory

interface CoinsRepository {
    suspend fun getCoinsList(): Result<CoinsListResponseDto, DataError.Remote>
    suspend fun getPriceHistory(coinId: String): Result<CoinPriceHistory, DataError.Remote>
    suspend fun getCoinDetailsOf(coinId: String): Result<CoinModel, DataError.Remote>
}