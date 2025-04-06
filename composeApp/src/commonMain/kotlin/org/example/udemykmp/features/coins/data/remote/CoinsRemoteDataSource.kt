package org.example.udemykmp.features.coins.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.example.udemykmp.core.data.remote.BASE_URL
import org.example.udemykmp.core.domain.DataError
import org.example.udemykmp.core.domain.Result
import org.example.udemykmp.core.network.safeCall
import org.example.udemykmp.features.coins.data.remote.dto.CoinDetailsResponseDto
import org.example.udemykmp.features.coins.data.remote.dto.CoinPriceHistoryResponseDto
import org.example.udemykmp.features.coins.data.remote.dto.CoinsListResponseDto

interface CoinsRemoteDataSource {
    suspend fun getCoinsList(): Result<CoinsListResponseDto, DataError.Remote>
    suspend fun getPriceHistory(coinId: String): Result<CoinPriceHistoryResponseDto, DataError.Remote>
    suspend fun getCoinDetailsOf(coinId: String): Result<CoinDetailsResponseDto, DataError.Remote>
}

class CoinsRemoteDataSourceImpl(
    private val httpClient: HttpClient
): CoinsRemoteDataSource {
    override suspend fun getCoinsList(): Result<CoinsListResponseDto, DataError.Remote> {
        return safeCall { httpClient.get("$BASE_URL/coins") }
    }

    override suspend fun getPriceHistory(coinId: String): Result<CoinPriceHistoryResponseDto, DataError.Remote> {
        return safeCall { httpClient.get("$BASE_URL/coin/$coinId/history") }
    }

    override suspend fun getCoinDetailsOf(coinId: String): Result<CoinDetailsResponseDto, DataError.Remote> {
        return safeCall { httpClient.get("$BASE_URL/coin/$coinId") }
    }
}
