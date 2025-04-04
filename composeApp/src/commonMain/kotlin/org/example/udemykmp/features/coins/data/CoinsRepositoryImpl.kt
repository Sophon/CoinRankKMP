package org.example.udemykmp.features.coins.data

import org.example.udemykmp.core.domain.DataError
import org.example.udemykmp.core.domain.Result
import org.example.udemykmp.core.domain.onError
import org.example.udemykmp.core.domain.onSuccess
import org.example.udemykmp.features.coins.data.mappers.toCoinModel
import org.example.udemykmp.features.coins.data.mappers.toPriceModel
import org.example.udemykmp.features.coins.data.remote.CoinsRemoteDataSource
import org.example.udemykmp.features.coins.integration.CoinsRepository
import org.example.udemykmp.features.coins.integration.model.CoinModel
import org.example.udemykmp.features.coins.integration.model.CoinPriceHistory

class CoinsRepositoryImpl(
    private val remoteDataSource: CoinsRemoteDataSource,
): CoinsRepository {

    override suspend fun getCoinsList(): Result<List<CoinModel>, DataError.Remote> {
        remoteDataSource.getCoinsList()
            .onSuccess { coinsListResponseDto ->
                val coins = coinsListResponseDto.data.coins.map { it.toCoinModel() }
                return Result.Success(coins)
            }
            .onError { return Result.Error(it) }

        return Result.Error(DataError.Remote.UNKNOWN)
    }

    override suspend fun getPriceHistory(coinId: String): Result<List<CoinPriceHistory>, DataError.Remote> {
        remoteDataSource.getPriceHistory(coinId)
            .onSuccess { coinPriceHistoryResponseDto ->
                val history = coinPriceHistoryResponseDto.data.history.map { it.toPriceModel() }
                return Result.Success(history)
            }
            .onError { return Result.Error(it) }

        return Result.Error(DataError.Remote.UNKNOWN)
    }

    override suspend fun getCoinDetailsOf(coinId: String): Result<CoinModel, DataError.Remote> {
        remoteDataSource.getCoinDetailsOf(coinId)
            .onSuccess { coinDetailsResponseDto ->
                val coin = coinDetailsResponseDto.data.coin.toCoinModel()
                return Result.Success(coin)
            }
            .onError { return Result.Error(it) }

        return Result.Error(DataError.Remote.UNKNOWN)
    }
}